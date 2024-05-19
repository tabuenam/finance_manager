package com.finance.manager.budget;

import com.finance.manager.budget.database.Budget;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    default Budget findByBudgetId(final Long budgetId) {
        return findById(budgetId).orElseThrow(EntityNotFoundException::new);
    }

    Set<Budget> findAllByUserId(@NotNull final Long userId);

    Set<Budget> findByUserIdAndCategoryId(@NotNull final Long userId, @NotNull final Long categoryId);
}
