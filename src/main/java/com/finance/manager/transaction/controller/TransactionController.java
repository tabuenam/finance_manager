package com.finance.manager.transaction.controller;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping
    public ResponseEntity<?> addTransactions(
            @Valid @RequestBody List<TransactionModel> transactionModels) {
        transactionService.addTransaction(transactionModels);
        return ResponseEntity.noContent().build();
    }
}
