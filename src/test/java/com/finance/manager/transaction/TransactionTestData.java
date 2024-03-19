package com.finance.manager.transaction;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.util.TransactionType;

public class TransactionTestData {
    public static TransactionModel buildTransactionModel() {
        return new TransactionModel(
                1234.90f,
                TransactionType.EXPENSE,
                1L,
                "transaction model notes"
        );
    }
}
