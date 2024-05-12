package com.finance.manager.transaction.controller;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.service.TransactionService;
import com.finance.manager.transaction.util.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

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
        transactionService.createTransaction(transactionModels);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTransaction(
            @Valid @RequestBody TransactionModel transactionModel) {
        transactionService.updateTransaction(transactionModel);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE') && hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/transaction-id/{transaction-id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable(name = "transaction-id") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping
    public ResponseEntity<?> getTransactions(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(transactionService.getAllTransactions(of(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping(value = "/transaction-id/{transaction-id")
    public ResponseEntity<?> getTransactionById(@PathVariable(value = "transaction-id") Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping(value = "/transaction-type/{transaction-type")
    public ResponseEntity<?> getTransactionById(@PathVariable(value = "transaction-type") @NotNull TransactionType transactionType,
                                                @RequestParam(defaultValue = "0") int pageNumber,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(transactionService.getTransactionByType(
                        transactionType,
                        of(pageNumber, pageSize)
                )
        );
    }
}
