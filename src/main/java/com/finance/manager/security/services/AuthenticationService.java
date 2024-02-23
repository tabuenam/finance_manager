package com.finance.manager.security.services;

import com.finance.manager.security.database.RefreshTokenEntity;
import com.finance.manager.security.model.AuthResponseModel;
import com.finance.manager.security.repository.RefreshTokenRepository;
import com.finance.manager.security.util.TokenType;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import com.finance.manager.user.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public AuthResponseModel getTokenAfterAuthentication(final Authentication authentication, final HttpServletResponse response) {
        try {
            var userEntity = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        System.out.println("[AuthService:userSignInAuth] User :{} not found " + authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, " USER NOT FOUND ");
                    });

            String refreshToken = jwtTokenGenerationService.generateRefreshToken(authentication);
            saveUserRefreshToken(userEntity, refreshToken);
            createRefreshTokenCookie(response, refreshToken);

            String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);
            return new AuthResponseModel(accessToken, 1 * 60, userEntity.getUsername(), TokenType.Bearer);
        } catch (Exception e) {
            System.out.println("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }

    public Object getAccessTokenUsingRefreshToken(final String authorizationHeader) {
        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }
        final String refreshToken = authorizationHeader.substring(7);

        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(token -> !token.getRevoked())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        UserEntity userEntity = refreshTokenEntity.getUser();
        Authentication authentication = createAuthenticationObject(userEntity);
        String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);

        return new AuthResponseModel(accessToken, 1 * 60, userEntity.getUsername(), TokenType.Bearer);
    }

    public AuthResponseModel registerUser(final UserModel userModel,
                                          final HttpServletResponse httpServletResponse) {
        try {
            Optional<UserEntity> optUserEntity = userRepository.findByEmail(userModel.email());

            if (optUserEntity.isPresent()) {
                throw new Exception("User exists");
            }

            UserEntity userEntity = userService.saveUser(userModel);

            Authentication authentication = createAuthenticationObject(userEntity);
            String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerationService.generateRefreshToken(authentication);

            UserEntity savedUserDetails = userRepository.save(userEntity);
            saveUserRefreshToken(userEntity, refreshToken);
            createRefreshTokenCookie(httpServletResponse, refreshToken);

            return new AuthResponseModel(accessToken, 1 * 60, savedUserDetails.getUsername(), TokenType.Bearer);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
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

    private void createRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
        response.addCookie(refreshTokenCookie);
    }

    private void saveUserRefreshToken(final UserEntity userEntity, final String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.saveAndFlush(refreshTokenEntity);
    }
}

