package com.finance.manager.transaction.model;

import com.finance.manager.transaction.util.TransactionType;
import jakarta.validation.constraints.NotNull;

public record TransactionModel(
        @NotNull
        Long userId,
        float amount,
        TransactionType transactionType
) {
}
