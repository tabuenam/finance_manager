package com.finance.manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/authenticate")
    public AuthResponseDto authenticate(Authentication authentication) {
        System.out.println("Hello endpoint authenticate");
        return authenticationService.getTokenAfterAuthentication(authentication);
    }
}
