package com.finance.manager.security.services;

import com.finance.manager.user.database.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    public String extractUserName(final Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(final Jwt jwtToken, UserEntity userEntity){
        String userName =  extractUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userEntity.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;
    }

    private boolean getIfTokenIsExpired(final Jwt jwtToken) {
        return requireNonNull(jwtToken.getExpiresAt())
                .isBefore(Instant.now());
    }
}
