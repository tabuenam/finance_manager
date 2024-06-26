package com.finance.manager.transaction.database;

import com.finance.manager.transaction.util.TransactionType;
import com.finance.manager.transaction.util.TransactionTypeConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@Table(name = "transactions")
@DynamicUpdate
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long transactionId;
    @Column(name = "user_id", nullable = false, unique = true)
    Long userId;
    @Column(name = "category_id", nullable = false)
    Long categoryId;
    @Column(name = "transaction_type", nullable = false)
    @Convert(converter = TransactionTypeConverter.class)
    TransactionType transactionType;
    @Column(name = "transaction_date", nullable = false)
    LocalDate transactionDate;
    @Column(name = "amount", nullable = false)
    Float amount;
    @Column(name = "notes")
    String notes;
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;
}
