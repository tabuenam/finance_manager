package com.finance.manager.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdatePasswordRequestModel(
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
