package com.finance.manager.transaction.controller;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.service.TransactionService;
import com.finance.manager.transaction.util.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createTransactions(
            @Valid @RequestBody List<TransactionModel> transactionModels) {
        transactionService.createTransaction(transactionModels);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PutMapping
    public ResponseEntity<?> updateTransaction(@Valid @NotNull TransactionModel transactionModel) {
        transactionService.updateTransaction(transactionModel);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(@NotNull Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping
    public ResponseEntity<?> getAllTransactions(
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                transactionService.getAllTransactions(of(pageNumber, pageSize)), HttpStatus.OK
        );
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping(value = "/transaction-id/{transaction-id}")
    public ResponseEntity<?> getTransactionById(
            @PathParam(value = "transaction-id") @NotNull Long transactionId
    ) {
        return new ResponseEntity<>(transactionService.getTransactionById(transactionId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping(value = "/transaction-type/{transaction-type}")
    public ResponseEntity<?> getTransactionByType(
            @PathParam(value = "transaction-Type") @NotNull TransactionType transactionType,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return new ResponseEntity<>(
                transactionService.getTransactionByType(transactionType, of(pageNumber, pageSize)), HttpStatus.OK
        );
    }
}
