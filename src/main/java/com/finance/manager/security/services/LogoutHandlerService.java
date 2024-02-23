package com.finance.manager.security.services;

import com.finance.manager.security.repository.RefreshTokenRepository;
import com.finance.manager.security.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutHandlerService implements LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(!authHeader.startsWith(TokenType.Bearer.name())){
            return;
        }

        final String refreshToken = authHeader.substring(7);

        var storedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(token->{
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                    return token;
                })
                .orElse(null);
    }
}
