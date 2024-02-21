package com.finance.manager.security.controller;

import com.finance.manager.security.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(Authentication authentication, HttpServletResponse response) {
        System.out.println("Hello endpoint authenticate");
        return ResponseEntity.ok(authenticationService.getTokenAfterAuthentication(authentication, response));
    }
}
