package com.finance.manager.transaction.repository;

import com.finance.manager.transaction.database.Transaction;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(@NotNull Long transactionId);
    Set<Transaction> findAllByUserId(@NotNull Long userId);
}
