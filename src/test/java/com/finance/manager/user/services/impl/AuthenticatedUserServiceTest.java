package com.finance.manager.user.services.impl;

import com.finance.manager.user.database.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.finance.manager.user.services.impl.UserServiceImplTestData.buildUserEntity;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticatedUserServiceTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticatedUserService underTest;

    @BeforeEach
    void setUp() {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void itShouldReturnTheAuthenticatedUserFromTheSecurityHoldContext() {
        //Arrange
        when(userService.findByUserMail(anyString()))
                .thenReturn(buildUserEntity());
        //Act
        UserEntity authenticatedUser = underTest.getAuthenticatedUser();
        //Assert
        assertNotNull(authenticatedUser);
    }
}