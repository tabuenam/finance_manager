package com.finance.manager.transaction.controller;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping
    public ResponseEntity<?> createTransactions(
            @Valid @RequestBody List<TransactionModel> transactionModels) {
        transactionService.addTransaction(transactionModels);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTransaction(@Valid TransactionModel transactionModel) {
        return new ResponseEntity<>(
                transactionService.updateTransaction(transactionModel), HttpStatus.OK
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(@NotNull  Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
}
