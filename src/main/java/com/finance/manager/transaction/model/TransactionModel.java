package com.finance.manager.transaction.model;

import com.finance.manager.transaction.util.TransactionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record TransactionModel(
        @NotNull
        Long transactionId,
        @Digits(integer = 4, fraction = 2)
        float amount,
        @NotNull
        TransactionType transactionType,
        @NotNull
        Long categoryId,
        String notes
) {
}
