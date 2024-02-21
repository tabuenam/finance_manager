package com.finance.manager.security.services;

import com.finance.manager.security.util.RSAKeyRecord;
import com.finance.manager.security.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JwtTokenAccessFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final RSAKeyRecord rsaKeyRecord;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

            if(!authHeader.startsWith(TokenType.BEARER.name())){
                filterChain.doFilter(request,response);
                return;
            }

            final String token = authHeader.substring(7);
            final Jwt jwtToken = jwtDecoder.decode(token);

            final String userName = jwtTokenUtils.extractUserName(jwtToken);
            if(!userName.isEmpty() && isNull(SecurityContextHolder.getContext().getAuthentication())){

                UserDetails userDetails = jwtTokenUtils.getUserDetails(userName);

                if(jwtTokenUtils.isTokenValid(jwtToken, userDetails)){
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);

                }
            }
            filterChain.doFilter(request,response);
        }catch (JwtValidationException jwtValidationException){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,jwtValidationException.getMessage());
        }
    }
}
