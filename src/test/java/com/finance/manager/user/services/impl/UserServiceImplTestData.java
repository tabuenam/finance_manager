package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UpdatePasswordRequestModel;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.roles.Role;

import java.time.LocalDateTime;

public class UserServiceImplTestData {
    static protected UserModel buildUserModel() {
        return new UserModel(
                "exampleUser",
                "example@email.com",
                "Example_9010",
                Role.USER
        );
    }

    static protected UpdatePasswordRequestModel buildUpdatePasswordRequest() {
        return new UpdatePasswordRequestModel(
                "email@example.com",
                "currentPassword_!",
                "currentPassword_!",
                "newPasswordStrong_19990!!%"
        );
    }

    public static UserEntity buildUserEntity() {
        return UserEntity.builder()
                .email("email@example.com")
                .username("exampleUser")
                .passwordHash("examplePassword")
                .role(Role.USER)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
