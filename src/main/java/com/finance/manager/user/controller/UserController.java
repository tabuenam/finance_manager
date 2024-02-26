package com.finance.manager.user.controller;

import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/mail/{mail}")
    public ResponseEntity<?> getAccountDetails(@PathParam("mail") @Email String mail){
        return ResponseEntity.ok(userService.getUserAccountDetail(mail));
    }

}
