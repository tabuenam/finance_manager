package com.finance.manager.budget.service;

import com.finance.manager.budget.BudgetRepository;
import com.finance.manager.budget.database.Budget;
import com.finance.manager.category.repository.CategoryRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public void addBudgetByCategory(final Long categoryId,
                                    final Float budgetAmount) {
        boolean isCategoryAvailable = categoryRepository.existsById(categoryId);

        if (!isCategoryAvailable) throw new NoSuchElementException();

        List<Budget> monthlyBudgets = Arrays.stream(Month.values())
                .map(month -> buildBudget(month, categoryId, budgetAmount))
                .toList();

        budgetRepository.saveAll(monthlyBudgets);
    }

    public void deleteBudget(final Long categoryId) {
        UserEntity authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        Set<Budget> budgets = budgetRepository.findByUserIdAndCategoryId(authenticatedUser.getId(), categoryId);

        budgetRepository.deleteAll(budgets);
    }

    private Budget buildBudget(final Month month,
                               final Long categoryId,
                               final Float budgetAmount) {
        UserEntity authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        int year = LocalDate.now().getYear();

        return Budget.builder()
                .userId(authenticatedUser.getId())
                .categoryId(categoryId)
                .amount(budgetAmount)
                .month(LocalDate.of(year, month, 1))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private double calculateMonthlyGeneralBudget() {
        UserEntity authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        Set<Budget> budgetsOfActualMonth = budgetRepository.findAllByUserId(authenticatedUser.getId())
                .stream().filter(budget -> budget.getMonth().getMonth() == LocalDate.now().getMonth())
                .collect(toSet());

        var budgetOfMonth = budgetsOfActualMonth
                .parallelStream()
                .mapToDouble(Budget::getAmount)
                .sum();

        return budgetOfMonth;
    }
}
