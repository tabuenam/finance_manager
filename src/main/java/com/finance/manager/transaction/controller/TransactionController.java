package com.finance.manager.transaction.controller;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.model.UpdateTransactionRequest;
import com.finance.manager.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> addUserTransaction(
            @Valid @RequestBody List<TransactionModel> transactionModels) {
        transactionService.addTransaction(transactionModels);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTransaction(
            @Valid @RequestBody UpdateTransactionRequest updateTransactionRequest) {
        transactionService.updateTransaction(updateTransactionRequest);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE') && hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/transaction-id/{transaction-id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable(name = "transaction-id") Long transactionId ) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

}
