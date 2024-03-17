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

import static com.finance.manager.user.services.impl.UserServiceImplTestData.buildUserEntity;
import static org.junit.jupiter.api.Assertions.*;
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

    }

    private TransactionModel buildTransactionModel() {
        return new TransactionModel(
                1234.90f,
                TransactionType.EXPENSE,
                1L
        );
    }


}