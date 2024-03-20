package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import jakarta.persistence.EntityNotFoundException;
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

    public TransactionModel updateTransaction(final TransactionModel transactionModel) {
        Transaction transaction = transactionRepository.findById(transactionModel.transactionId())
                .orElseThrow(EntityNotFoundException::new);

        transaction.setTransactionType(transactionModel.transactionType());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setNotes(transactionModel.notes());
        transaction.setAmount(transactionModel.amount());
        transaction.setCategoryId(transactionModel.categoryId());

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToModel(updatedTransaction);
    }

    public void deleteTransaction(final Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    protected TransactionModel mapToModel(final Transaction entity) {
        return new TransactionModel(
                entity.getTransactionId(),
                entity.getAmount(),
                entity.getTransactionType(),
                entity.getCategoryId(),
                entity.getNotes()
        );
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
