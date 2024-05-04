package com.finance.manager.transaction.service;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.model.TransactionModel;
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
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public void addTransaction(final List<TransactionModel> transactionModels) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByUserMail(authentication.getName());

        Set<Transaction> transactions = transactionModels.stream()
                .map(transactionModel -> buildTransaction(transactionModel, user))
                .collect(toSet());

        transactionRepository.saveAll(transactions);
    }

    private Transaction buildTransaction(final TransactionModel transaction, final UserEntity user) {
        return Transaction.builder()
                .transactionDate(LocalDate.now())
                .transactionType(transaction.transactionType())
                .categoryId(transaction.categoryId())
                .userId(user.getId())
                .amount(transaction.amount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
