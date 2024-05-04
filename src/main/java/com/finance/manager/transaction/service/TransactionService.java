package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.model.UpdateTransactionRequest;
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
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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

    public void updateTransaction(final TransactionModel transactionModel) {
        transactionRepository.findById(transactionModel.transactionId())
                .ifPresentOrElse(
                        transaction -> updateTransactionInDb(transactionModel, transaction),
                        () -> {
                            throw new EntityNotFoundException("Transaction could not be found");
                        }
                );
    }

    private void updateTransactionInDb(final TransactionModel transactionModel, Transaction transaction) {
        transaction.setTransactionType(transactionModel.transactionType());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setNotes(transactionModel.notes());
        transaction.setAmount(transactionModel.amount());
        transaction.setCategoryId(transactionModel.categoryId());
        transactionRepository.save(transaction);
    }

    public void deleteTransaction(final Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
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

    protected Transaction mapToTransaction(final TransactionModel transactionModel, final Long userId) {
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
