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
                                String newPassword
)
{ }
