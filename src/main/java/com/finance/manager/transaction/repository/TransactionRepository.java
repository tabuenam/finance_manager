package com.finance.manager.transaction.repository;

import com.finance.manager.transaction.database.Transaction;
import com.finance.manager.transaction.util.TransactionType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    default Transaction findByTransactionId(@NotNull Long transactionId) {
        return findById(transactionId).orElseThrow(EntityNotFoundException::new);
    }

    Page<Transaction> findAllByUserId(@NotNull final Long userId,
                                      @NotNull Pageable pageable);

    Page<Transaction> findByTransactionTypeAndUserId(@NotNull final TransactionType transactionType,
                                                     @NotNull Long userId,
                                                     @NotNull Pageable pageable);
}
