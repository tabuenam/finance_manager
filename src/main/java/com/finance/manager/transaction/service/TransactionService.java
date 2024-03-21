package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.transaction.util.TransactionType;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public void createTransaction(final List<TransactionModel> transactionModels) {
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

    public Collection<TransactionModel> getAllTransactions(final Pageable pageable) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();
        return transactionRepository.findAllByUserId(user.getId(), pageable)
                .map(this::mapToModel)
                .toList();
    }

    public TransactionModel getTransactionById(final Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(this::mapToModel)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Collection<TransactionModel> getTransactionByType(final TransactionType transactionType,
                                                              final Pageable pageable) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();
        return transactionRepository
                .findByTransactionTypeAndUserId(transactionType, user.getId(), pageable)
                .map(this::mapToModel)
                .toList();
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
