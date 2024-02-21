package com.finance.manager.security.services;

import com.finance.manager.security.database.RefreshTokenEntity;
import com.finance.manager.security.model.AuthResponseModel;
import com.finance.manager.security.repository.RefreshTokenRepository;
import com.finance.manager.security.util.TokenType;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userInfoRepo;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseModel getTokenAfterAuthentication(final Authentication authentication, final HttpServletResponse response) {
        try {
            var userEntity = userInfoRepo.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        System.out.println("[AuthService:userSignInAuth] User :{} not found " + authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, " USER NOT FOUND ");
                    });

            String refreshToken = jwtTokenGenerationService.generateRefreshToken(authentication);
            saveUserRefreshToken(userEntity, refreshToken);
            creatRefreshTokenCookie(response, refreshToken);

            String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);
            return new AuthResponseModel(accessToken, 15 * 60, userEntity.getUsername(), TokenType.BEARER);
        } catch (Exception e) {
            System.out.println("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    private void saveUserRefreshToken(final UserEntity userEntity, final String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.saveAndFlush(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(final String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.BEARER.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.
        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(token -> !token.getRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        UserEntity userEntity = refreshTokenEntity.getUser();

        //Now create the Authentication object
        Authentication authentication = createAuthenticationObject(userEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role.
        String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);

        return new AuthResponseModel(accessToken, 15 * 60, userEntity.getUsername(), TokenType.BEARER);
    }

    private Authentication createAuthenticationObject(final UserEntity userInfoEntity) {
        List<GrantedAuthority> authorities = Stream.of(userInfoEntity.getRole().name())
                .map(role -> (GrantedAuthority) role::trim)
                .toList();

        return new UsernamePasswordAuthenticationToken(
                userInfoEntity.getUsername(),
                userInfoEntity.getPassword(),
                authorities
        );
    }
}

