package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.model.UpdateTransactionRequest;
import com.finance.manager.transaction.repository.TransactionRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public void addTransaction(final List<TransactionModel> transactionModels) {
        Set<Transaction> transactions = transactionModels.stream()
                .map(this::buildTransaction)
                .collect(toSet());
        transactionRepository.saveAll(transactions);
    }

    public void updateTransaction(final UpdateTransactionRequest updateTransactionRequest) {
        transactionRepository.findByTransactionId(updateTransactionRequest.transactionId())
                .ifPresentOrElse(
                        transaction -> updateFoundTransaction(
                                transaction,
                                updateTransactionRequest.transactionModel()
                        ),
                        () -> {
                            throw new NoSuchElementException("Transaction could not be found: " + updateTransactionRequest);
                        }
                );
    }

    private void updateFoundTransaction(Transaction transaction, final TransactionModel transactionModel) {
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setTransactionType(transactionModel.transactionType());
        transaction.setAmount(transactionModel.amount());
        transaction.setNotes(transactionModel.notes());
        transaction.setCategoryId(transactionModel.categoryId());
    }

    private Transaction buildTransaction(final TransactionModel transaction) {
        UserEntity user = getUserEntity();
        return Transaction.builder()
                .transactionDate(LocalDate.now())
                .transactionType(transaction.transactionType())
                .categoryId(transaction.categoryId())
                .userId(user.getId())
                .notes(transaction.notes())
                .amount(transaction.amount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void deleteTransaction(final Long transactionId) {
        transactionRepository.findByTransactionId(transactionId).ifPresentOrElse(
                transactionRepository::delete
                , () -> {
                    throw new NoSuchElementException("Transaction could not be found: " + transactionId);
                }
        );
    }

    public Set<TransactionModel> findTransactions() {
        UserEntity user = getUserEntity();
        return transactionRepository.findAllByUserId(user.getId()).stream()
                .map(this::buildTransactionModel)
                .collect(toSet());
    }

    private TransactionModel buildTransactionModel(final Transaction transaction) {
        return new TransactionModel(
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getCategoryId(),
                transaction.getNotes()
        );
    }

    private UserEntity getUserEntity() {
        Authentication authentication = getAuthentication();
        return userService.findByUserMail(authentication.getName());
    }
}
