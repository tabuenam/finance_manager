package com.finance.manager.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.user.model.UpdatePasswordRequest;
import com.finance.manager.user.model.UserAccountDetailModel;
import com.finance.manager.user.roles.Role;
import com.finance.manager.user.services.impl.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void itShouldGetTheAccountDetailsOfASpecificUser() throws Exception {
        //Arrange
        String mail = "user@mail.com";

        var userAccountDetailModel = getUserAccountDetailModel();
        when(userService.getUserAccountDetail(any()))
                .thenReturn(userAccountDetailModel);
        //Act
        ResultActions resultActions =
                mockMvc.perform(get("/api/v1/users/mail/{mail}", mail)
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .characterEncoding("UTF-8")
                );
        //Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is(userAccountDetailModel.userName())))
                .andExpect(jsonPath("$.email", is(userAccountDetailModel.email())));

        verify(userService, times(1))
                .getUserAccountDetail(any());
    }

    @Test
    void itShouldSuccessfullyUpdatePasswordOfUser() throws Exception {
        //Arrange
        UpdatePasswordRequest updatePasswordRequest
                = getUpdatePasswordRequest();
        UserAccountDetailModel userAccountDetailModel
                = getUserAccountDetailModel();

        when(userService.updatePassword(any()))
                .thenReturn(userAccountDetailModel);
        //Act
        ResultActions resultActions =
                mockMvc.perform(post("/api/v1/users/update-password")
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordRequest))
                );
        //Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is(userAccountDetailModel.userName())))
                .andExpect(jsonPath("$.email", is(userAccountDetailModel.email())));
    }


    private UserAccountDetailModel getUserAccountDetailModel() {
        return new UserAccountDetailModel(
                "userName",
                "user@mail.com",
                Role.USER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private UpdatePasswordRequest getUpdatePasswordRequest() {
        return new UpdatePasswordRequest(
                "user@mail.com",
                "strongPassword",
                "strongPassword",
                "$trong%!P@sswOrd_1994?"
        );
    }
}