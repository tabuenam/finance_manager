package com.finance.manager.security;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/authenticate")
    public String authenticate(@RequestBody LoginRequest loginRequest) {
        System.out.println("Hello endpoint authenticate");
        return authenticationService.authenticateUser(loginRequest);
    }
}
