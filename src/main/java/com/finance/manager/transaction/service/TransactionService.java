package com.finance.manager.transaction.service;

import com.finance.manager.Mapper;
import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.transaction.util.TransactionType;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final Mapper<Transaction, TransactionModel> mapper;
    private final AuthenticatedUserService authenticatedUserService;

    public void createTransaction(final List<TransactionModel> transactionModels) {
        List<Transaction> transactions = transactionModels
                .stream()
                .map(mapper::mapToEntity)
                .toList();
        transactionRepository.saveAll(transactions);
    }

    public void updateTransaction(final TransactionModel transactionModel) {
        var transactionToUpdate = transactionRepository.findByTransactionId(transactionModel.transactionId());
        updateTransactionInDb(transactionModel, transactionToUpdate);
    }

    private void updateTransactionInDb(final TransactionModel transactionModel, Transaction transaction) {
        transaction.setTransactionType(transactionModel.transactionType());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setNotes(transactionModel.notes());
        transaction.setAmount(transactionModel.amount());
        transaction.setCategoryId(transactionModel.categoryId());
        transactionRepository.save(transaction);
    }

    public Collection<TransactionModel> getAllTransactions(final Pageable pageable) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();
        return transactionRepository.findAllByUserId(user.getId(), pageable)
                .map(mapper::mapToModel)
                .toList();
    }

    public TransactionModel getTransactionById(final Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId);
        return mapper.mapToModel(transaction);
    }

    public Collection<TransactionModel> getTransactionByType(final TransactionType transactionType,
                                                             final Pageable pageable) {
        UserEntity user = authenticatedUserService.getAuthenticatedUser();
        return transactionRepository
                .findByTransactionTypeAndUserId(transactionType, user.getId(), pageable)
                .map(mapper::mapToModel)
                .toList();
    }

    public void deleteTransaction(final Long transactionId) {
        Transaction transaction = transactionRepository.findByTransactionId(transactionId);
        transactionRepository.delete(transaction);
    }
}
