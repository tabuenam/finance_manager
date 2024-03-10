package com.finance.manager.user.services.impl;

import com.finance.manager.email.service.MailService;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UpdatePasswordRequestModel;
import com.finance.manager.user.model.UserAccountDetailModel;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import com.finance.manager.user.roles.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.finance.manager.user.roles.Role.ADMIN;
import static com.finance.manager.user.roles.Role.USER;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public UserEntity saveUser(final UserModel userModel) {
        UserEntity userEntity = UserEntity.builder()
                .email(userModel.email())
                .username(userModel.userName())
                .passwordHash(passwordEncoder.encode(userModel.password()))
                .role(determineUserRole(userModel))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mailService.sendMail(
                userModel.email(),
                "UserAccount at finance-manager",
                "Hello Juan, I would like to welcome you to your financial future.");

        return userRepository.saveAndFlush(userEntity);
    }

    public UserAccountDetailModel getUserAccountDetail(final String mail) {
        UserEntity userEntity = userRepository.findByEmail(mail)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        return new UserAccountDetailModel(
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );
    }

    public UserAccountDetailModel updatePassword(final UpdatePasswordRequestModel updatePasswordRequestModel) {
        UserEntity userEntity = userRepository.findByEmail(updatePasswordRequestModel.email())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if (!updatePasswordRequestModel.currentPassword().equals(updatePasswordRequestModel.confirmationPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        userEntity.setPasswordHash(passwordEncoder.encode(updatePasswordRequestModel.newPassword()));
        userEntity.setUpdatedAt(LocalDateTime.now());
        UserEntity updatedUserEntity = userRepository.saveAndFlush(userEntity);

        return new UserAccountDetailModel(
                updatedUserEntity.getUsername(),
                updatedUserEntity.getEmail(),
                updatedUserEntity.getRole(),
                updatedUserEntity.getCreatedAt(),
                updatedUserEntity.getUpdatedAt()
        );
    }

    public void deleteUserAccount(final String email) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new RuntimeException("User does not exist");
                        }
                );

    }

    private Role determineUserRole(final UserModel userModel) {
        return isNull(userModel.role()) ? USER : ADMIN;
    }
}
