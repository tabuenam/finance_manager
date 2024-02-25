package com.finance.manager.user.repository;

import com.finance.manager.user.database.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.finance.manager.user.services.impl.UserServiceImplTestData.buildUserEntity;

@DataJpaTest
@Disabled
class UserRepositoryTest {
    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindAUserEntityByEmailSuccessfully() {
        //TODO: @DataJpaTest annotation needs to be overriden -> profile configuration
        //Arrange
        String validEmail = "email@example.com";

        UserEntity userEntity = buildUserEntity();
        underTest.saveAndFlush(userEntity);

        //Act
        Optional<UserEntity> optionalUserEntity = underTest.findByEmail(validEmail);

        //Assert
        Assertions.assertTrue(optionalUserEntity.isPresent());
    }
}