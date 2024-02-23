package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UpdatePasswordRequest;
import com.finance.manager.user.model.UserAccountDetailModel;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import com.finance.manager.user.roles.Role;
import com.finance.manager.user.services.UserService;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        return userRepository.saveAndFlush(userEntity);
    }

    public UserAccountDetailModel getUserAccountDetail(final UserModel userModel) {
        return userRepository.findByEmail(userModel.email())
                .map(userEntity -> {
                            return new UserAccountDetailModel(
                                    userEntity.getUsername(),
                                    userEntity.getEmail(),
                                    userEntity.getRole(),
                                    userEntity.getCreatedAt(),
                                    userEntity.getUpdatedAt()
                            );
                        }
                ).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public UserAccountDetailModel updatePassword(final UpdatePasswordRequest updatePasswordRequest) {
        UserEntity userEntity = userRepository.findByEmail(updatePasswordRequest.email())
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        if (!updatePasswordRequest.currentPassword().equals(updatePasswordRequest.confirmationPassword())) {
            throw new RuntimeException("Password do not match");
        }

        userEntity.setPasswordHash(passwordEncoder.encode(updatePasswordRequest.newPassword()));
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

    public void deleteUserAccount(final String email){
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User does not exist"));

        userRepository.delete(userEntity);
        System.out.println("User has been deleted");
    }


    private Role determineUserRole(final UserModel userModel) {
        return isNull(userModel.role()) ? USER : ADMIN;
    }
}
