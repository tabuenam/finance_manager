package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {
    private final UserService userService;

    public UserEntity getAuthenticatedUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUserMail(authentication.getName());
    }
}
