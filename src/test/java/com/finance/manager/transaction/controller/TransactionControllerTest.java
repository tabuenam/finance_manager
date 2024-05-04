package com.finance.manager.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static com.finance.manager.transaction.TransactionTestData.buildTransactionModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private TransactionModel transactionModel;

    @BeforeEach
    void setUp() {
        transactionModel = buildTransactionModel();
    }

    @Test
    void itShouldCreateNewTransactions() throws Exception {
        //Arrange
        doNothing().when(transactionService)
                .createTransaction(any());
        //Act
        ResultActions resultActions =
                mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(transactionModel)))
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .characterEncoding("UTF-8")
                );
        //Assert
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

        verify(transactionService, times(1))
                .createTransaction(any());
    }
}