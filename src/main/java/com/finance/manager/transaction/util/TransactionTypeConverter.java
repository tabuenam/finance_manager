package com.finance.manager.transaction.util;

import jakarta.persistence.AttributeConverter;

public class TransactionTypeConverter
        implements AttributeConverter<TransactionType, String> {
    @Override
    public String convertToDatabaseColumn(final TransactionType transactionType) {
        return transactionType.name();
    }

    @Override
    public TransactionType convertToEntityAttribute(final String transactionTypeString) {
        switch (transactionTypeString) {
            case "Income" -> {
                return TransactionType.INCOME;
            }
            case "Expense" -> {
                return TransactionType.EXPENSE;
            }
            default -> {
                return TransactionType.UNKNOWN;
            }
        }
    }
}
