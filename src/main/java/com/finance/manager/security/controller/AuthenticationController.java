package com.finance.manager.security.controller;

import com.finance.manager.security.services.AuthenticationService;
import com.finance.manager.user.model.UserModel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticate(Authentication authentication, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.getTokenAfterAuthentication(authentication, response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return ResponseEntity.ok(authenticationService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserModel userModel,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse){

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        return ResponseEntity.ok(authenticationService.registerUser(userModel,httpServletResponse));
    }
}
