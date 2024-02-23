package com.finance.manager.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(
                                @Email
                                @NotNull
                                String email,
                                @NotNull
                                String currentPassword,
                                @NotNull
                                String confirmationPassword,
                                @NotNull
                                @NotNull
                                @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,}$")
                                String newPassword
)
{ }
