package com.finance.manager.transaction;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.util.TransactionType;

public class TransactionTestData {
    public static Transaction buildTransactionEntity(){
        return Transaction.builder()
                .transactionId(1L)
                .notes("transaction notes")
                .amount(1234f)
                .transactionType(TransactionType.EXPENSE)
                .userId(1L)
                .categoryId(1L)
                .build();
    }
    public static TransactionModel buildTransactionModel() {
        return new TransactionModel(
                1L,
                1234.90f,
                TransactionType.EXPENSE,
                1L,
                "transaction model notes"
        );
    }
}
