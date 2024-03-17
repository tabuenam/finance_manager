package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.model.UpdatePasswordRequestModel;
import com.finance.manager.user.model.UserAccountDetailModel;
import com.finance.manager.user.model.UserModel;
import com.finance.manager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.finance.manager.user.services.impl.UserServiceImplTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService underTest;
    private UserEntity userEntity;
    private UserModel userModel;
    private UpdatePasswordRequestModel updatePasswordRequestModel;

    @BeforeEach
    void setUp() {
        userEntity = buildUserEntity();
        userModel = buildUserModel();
        updatePasswordRequestModel = buildUpdatePasswordRequest();
    }

    @Test
    void itShouldSaveAUserSuccessfullyInTheDatabase() {
        //Arrange
        when(passwordEncoder.encode(anyString()))
                .thenReturn(anyString());
        //Act
        underTest.saveUser(userModel);
        //Assert
        verify(userRepository, times(1))
                .saveAndFlush(any(UserEntity.class));
        verify(passwordEncoder, times(1))
                .encode(anyString());
    }

    @Test
    void itShouldGetUserAccountDetailsOfUser() {
        //Arrange
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(userEntity));
        //Act

        UserAccountDetailModel userAccountDetail =
                underTest.getUserAccountDetail("user@email.com");
        //Assert
        assertEquals(userEntity.getUsername(), userAccountDetail.userName());
        assertEquals(userEntity.getEmail(), userAccountDetail.email());
        assertEquals(userEntity.getRole(), userAccountDetail.role());
        assertEquals(userEntity.getCreatedAt(), userAccountDetail.createdAt());

        verify(userRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void itShouldThrowAnExceptionUserEmailNotFoundInDb() {
        //Arrange
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        //Act
        assertThrows(RuntimeException.class, () ->
                underTest.getUserAccountDetail("user@email.com"));
        //Assert
        verify(userRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void itShouldUpdateThePasswordOfUserSuccessfullyInDb() {
        //Arrange
        String newEncodedPassword = "Pasdfölajsdf2ö3oi";

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(userEntity));

        when(passwordEncoder.encode(anyString()))
                .thenReturn(newEncodedPassword);

        when(userRepository.saveAndFlush(any(UserEntity.class)))
                .thenReturn(userEntity);
        //Act
        UserAccountDetailModel userAccountDetailModel =
                underTest.updatePassword(updatePasswordRequestModel);
        //Assert
        assertEquals(userEntity.getUsername(), userAccountDetailModel.userName());
        assertEquals(userEntity.getEmail(), userAccountDetailModel.email());
        assertEquals(userEntity.getRole(), userAccountDetailModel.role());
        assertEquals(userEntity.getCreatedAt(), userAccountDetailModel.createdAt());

        verify(userRepository, times(1))
                .findByEmail(anyString());
        verify(passwordEncoder, times(1))
                .encode(anyString());
        verify(userRepository, times(1))
                .saveAndFlush(any(UserEntity.class));
    }

    @Test
    void itShouldThrowAnExceptionUserEmailNotFoundInDbWhileUpdatingPassword() {
        //Arrange
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        //Act
        assertThrows(RuntimeException.class, () ->
                underTest.updatePassword(updatePasswordRequestModel));
        //Assert
        verify(userRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void itShouldThrowAnExceptionUserEmailNotFoundInDbWhileUpdatingPasswordPasswordDoNotMatch() {
        //Arrange
        UpdatePasswordRequestModel invalidPasswordUpdateRequest = new UpdatePasswordRequestModel(
                "email@example.com",
                "currentPassword_!",
                "DONOTMATCH",
                "newPasswordStrong_19990!!%"
        );

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(userEntity));
        //Act
        assertThrows(RuntimeException.class, () ->
                underTest.updatePassword(invalidPasswordUpdateRequest));
        //Assert
        verify(userRepository, times(1))
                .findByEmail(anyString());
    }

    @Test
    void itShouldDeleteTheAccountOfUserSuccessfully() {
        //Arrange
        String validEmail = "example@example.com";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository)
                .delete(any(UserEntity.class));
        //Act
        underTest.deleteUserAccount(validEmail);

        //Assert
        verify(userRepository, times(1))
                .findByEmail(anyString());
        verify(userRepository, times(1))
                .delete(any(UserEntity.class));
    }

    @Test
    void itShouldThrowAnExceptionUserEmailNotFoundInDbWhileDeletingUserAccount() {
        //Arrange
        String validEmail = "example@example.com";
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        //Act
        assertThrows(RuntimeException.class, () ->
                underTest.deleteUserAccount(validEmail));
        //Assert
        verify(userRepository, times(1))
                .findByEmail(anyString());
    }
}