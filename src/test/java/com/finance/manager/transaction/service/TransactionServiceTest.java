package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.finance.manager.transaction.TransactionTestData.buildTransactionEntity;
import static com.finance.manager.transaction.TransactionTestData.buildTransactionModel;
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
    private TransactionModel transactionModel;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        userEntity = buildUserEntity();
        transactionModel = buildTransactionModel();
        transaction = buildTransactionEntity();
    }

    @Test
    void itShouldMapToATransactionEntitySuccessfully() {
        Transaction transaction =
                underTest.mapToTransaction(transactionModel, userEntity.getId());

        assertAll("transactionEntity",
                () -> assertNotNull(transaction),
                () -> assertEquals(userEntity.getId(), transaction.getUserId()),
                () -> assertEquals(transactionModel.transactionType(), transaction.getTransactionType()),
                () -> assertEquals(transactionModel.amount(), transaction.getAmount()),
                () -> assertEquals(transactionModel.categoryId(), transaction.getCategoryId())
        );
    }

    @Test
    void itShouldPersistNewTransactionsToTheDB() {
        //Arrange
        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(userEntity);
        List<TransactionModel> transactionModels =
                List.of(transactionModel);
        //Act
        underTest.createTransaction(transactionModels);
        //Assert
        verify(authenticatedUserService, times(1))
                .getAuthenticatedUser();
        verify(transactionRepository, times(1))
                .saveAll(anyIterable());
    }


    @Test
    void itShouldUpdateTransactionInTheDb() {
        when(transactionRepository.findById(anyLong()))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(any()))
                .thenReturn(transaction);

        underTest.updateTransaction(transactionModel);

        verify(transactionRepository, times(1))
                .findById(anyLong());
        verify(transactionRepository, times(1))
                .save(any());
    }
}