package com.finance.manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;

import static java.util.stream.Collectors.joining;
import static org.springframework.security.oauth2.jwt.JwtEncoderParameters.from;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public String generateToken(final Authentication authentication) {
        String scope = extractScope(authentication.getAuthorities());
        JwtClaimsSet claims = extractClaims(authentication, scope);

        JwtEncoderParameters encoderParameters = from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    private JwtClaimsSet extractClaims(final Authentication authentication, final String scope) {
        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
    }

    private String extractScope(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(" "));
    }
}
