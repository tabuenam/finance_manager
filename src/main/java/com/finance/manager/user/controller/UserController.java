package com.finance.manager.user.controller;

import com.finance.manager.user.model.UpdatePasswordRequest;
import com.finance.manager.user.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/mail/{mail}")
    public ResponseEntity<?> getAccountDetails(@PathParam("mail") @Email String mail) {
        return ResponseEntity.ok(userService.getUserAccountDetail(mail));
    }

    @PostMapping(value = "/update-password")
    public ResponseEntity<?> updateUserPassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(userService.updatePassword(updatePasswordRequest));
    }

    @DeleteMapping(value = "/mail/{mail}")
    public ResponseEntity<?> deleteAccount(@PathParam("mail") @Email String mail) {
        userService.deleteUserAccount(mail);
        return ResponseEntity.noContent().build();
    }


}
