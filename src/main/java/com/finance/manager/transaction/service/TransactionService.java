package com.finance.manager.transaction.service;

import com.finance.manager.transaction.model.TransactionModel;
import com.finance.manager.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    void addIncome(final List<TransactionModel> incomes){

    }

    void addExpense(final List<TransactionModel> expenses){

    }
}
