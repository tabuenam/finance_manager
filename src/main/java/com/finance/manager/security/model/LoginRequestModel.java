package com.finance.manager.security.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestModel(
        @NotNull
        @Email(message = "Email must to be provided")
        @NotBlank
        String email,
        @NotNull(message = "Password must be provided")
        @NotBlank
        String password) {
}
