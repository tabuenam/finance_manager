package com.finance.manager.transaction.model;

import jakarta.validation.constraints.NotNull;

public record UpdateTransactionRequest(
        @NotNull
        Long transactionId,
        @NotNull
        TransactionModel transactionModel
) {}
