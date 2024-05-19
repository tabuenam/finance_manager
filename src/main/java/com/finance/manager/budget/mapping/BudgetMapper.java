package com.finance.manager.budget.mapping;

import com.finance.manager.Mapper;
import com.finance.manager.budget.database.Budget;
import com.finance.manager.budget.model.BudgetModel;

public class BudgetMapper implements Mapper<Budget, BudgetModel> {
    @Override
    public BudgetModel mapToModel(Budget entity) {
        return null;
    }

    @Override
    public Budget mapToEntity(BudgetModel model) {
        return null;
    }
}
