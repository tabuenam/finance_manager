package com.finance.manager.category.model;

import jakarta.validation.constraints.NotNull;

public record CategoryModel(
        @NotNull
        String categoryName,
        String categoryDescription
) {
}
