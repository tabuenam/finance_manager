package com.finance.manager.user.model;

import com.finance.manager.user.roles.Role;

public record UserModel(String userName, String email, String password, Role role) {
}
