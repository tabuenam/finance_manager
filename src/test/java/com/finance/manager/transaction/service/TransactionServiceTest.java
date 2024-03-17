package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.transaction.util.TransactionType;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.finance.manager.user.services.impl.UserServiceImplTestData.buildUserEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AuthenticatedUserService authenticatedUserService;
    @InjectMocks
    private TransactionService underTest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = buildUserEntity();
    }

    @Test
    void itShouldMapToATransactionEntitySuccessfully(){
        TransactionModel transactionModel = buildTransactionModel();

        Transaction transaction =
                underTest.mapToTransaction(transactionModel, userEntity.getId());

        assertNotNull(transaction);
        assertEquals(userEntity.getId(), transaction.getUserId());
        assertEquals(transactionModel.transactionType(), transaction.getTransactionType());
        assertEquals(transactionModel.amount(), transaction.getAmount());
        assertEquals(transactionModel.categoryId(), transaction.getCategoryId());
    }

    @Test
    void itShouldPersistNewTransactionsToTheDB(){
        //Arrange
        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(userEntity);
        List<TransactionModel> transactionModels =
                List.of(buildTransactionModel());
        //Act
        underTest.addTransaction(transactionModels);
        //Assert
        verify(authenticatedUserService, times(1))
                .getAuthenticatedUser();
        verify(transactionRepository, times(1))
                .saveAll(anyIterable());

    }

    private TransactionModel buildTransactionModel() {
        return new TransactionModel(
                1234.90f,
                TransactionType.EXPENSE,
                1L,
                "transaction model notes"
        );
    }
}