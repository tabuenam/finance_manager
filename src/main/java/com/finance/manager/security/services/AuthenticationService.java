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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userInfoRepo;
    private final JwtTokenGenerationService jwtTokenGenerationService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseModel getTokenAfterAuthentication(final Authentication authentication,final HttpServletResponse response) {
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
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }
}

