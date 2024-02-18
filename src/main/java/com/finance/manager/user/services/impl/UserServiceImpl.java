package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import com.finance.manager.user.roles.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.finance.manager.user.roles.Role.ADMIN;
import static com.finance.manager.user.roles.Role.USER;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserServiceImpl{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser(final UserModel userModel) {
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

        userRepository.saveAndFlush(userEntity);
    }

    private Role determineUserRole(final UserModel userModel) {
        return isNull(userModel.role()) ? USER : ADMIN;
    }
}
