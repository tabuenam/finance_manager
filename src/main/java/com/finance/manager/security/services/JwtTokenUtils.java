package com.finance.manager.security.services;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import com.finance.manager.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    private final UserRepository userRepository;

    public String extractUserName(final Jwt jwtToken) {
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(final Jwt jwtToken, final UserDetails userDetails){
        String userName =  extractUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired && isTokenUserSameAsDatabase;
    }

    private boolean getIfTokenIsExpired(final Jwt jwtToken) {
        return requireNonNull(jwtToken.getExpiresAt())
                .isBefore(Instant.now());
    }

    public UserDetails getUserDetails(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User's email could not be found"));
    }
}
