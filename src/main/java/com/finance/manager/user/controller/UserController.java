package com.finance.manager.user.controller;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping(value ="/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserModel userModel
    ) {
        userService.saveUser(userModel);
        return ResponseEntity.ok().build();
    }
}
