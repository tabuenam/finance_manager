package com.finance.manager.transaction.model;

import com.finance.manager.transaction.util.TransactionType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionModel(
        float amount,
        @NotNull
        TransactionType transactionType,
        @NotNull
        Long categoryId,
        String notes
) {
}
