package com.finance.manager.user.model;

import com.finance.manager.user.roles.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserModel(
        @Size(max = 50)
        @NotNull
        String userName,
        @Email
        @NotNull
        String email,
        @NotNull
        @Size(min = 7, max = 60)
        String password,
        Role role) {
}
