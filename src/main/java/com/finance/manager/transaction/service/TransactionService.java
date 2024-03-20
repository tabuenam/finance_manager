package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public void addTransaction(final List<TransactionModel> transactionModels) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();

        List<Transaction> transactions = transactionModels.stream()
                .map(transactionModel -> mapToTransaction(transactionModel, user.getId()))
                .toList();
        transactionRepository.saveAll(transactions);
    }

    Transaction mapToTransaction(final TransactionModel transactionModel, final Long userId) {
        return Transaction.builder()
                .transactionDate(LocalDate.now())
                .transactionType(transactionModel.transactionType())
                .categoryId(transactionModel.categoryId())
                .userId(userId)
                .notes(transactionModel.notes())
                .amount(transactionModel.amount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
