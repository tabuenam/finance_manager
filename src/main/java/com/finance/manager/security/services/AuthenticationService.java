package com.finance.manager.security.services;

import com.finance.manager.security.model.AuthResponseModel;
import com.finance.manager.security.util.TokenType;
import com.finance.manager.user.repository.UserRepository;
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

    public AuthResponseModel getTokenAfterAuthentication(Authentication authentication) {
        try {
            var userInfoEntity = userInfoRepo.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        System.out.println("[AuthService:userSignInAuth] User :{} not found " + authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, " USER NOT FOUND ");
                    });

            String accessToken = jwtTokenGenerationService.generateAccessToken(authentication);
            return new AuthResponseModel(accessToken, 15 * 60, userInfoEntity.getUsername(), TokenType.BEARER);
        } catch (Exception e) {
            System.out.println("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
        }
    }
}

