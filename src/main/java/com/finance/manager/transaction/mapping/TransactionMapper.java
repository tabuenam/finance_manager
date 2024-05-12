package com.finance.manager.transaction.mapping;

import com.finance.manager.Mapper;
import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component("transaction")
@RequiredArgsConstructor
public class TransactionMapper implements Mapper<Transaction, TransactionModel> {
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public TransactionModel mapToModel(final Transaction entity) {
        return new TransactionModel(
                entity.getTransactionId(),
                entity.getAmount(),
                entity.getTransactionType(),
                entity.getCategoryId(),
                entity.getNotes()
        );
    }

    @Override
    public Transaction mapToEntity(final TransactionModel transactionModel) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();
        return Transaction.builder()
                .transactionDate(LocalDate.now())
                .transactionType(transactionModel.transactionType())
                .categoryId(transactionModel.categoryId())
                .userId(user.getId())
                .notes(transactionModel.notes())
                .amount(transactionModel.amount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
