package com.finance.manager.category;

import com.finance.manager.category.model.CategoryModel;

public class CategoryTestData {
    public static CategoryModel buildCategoryModel() {
        return new CategoryModel(
                "House",
                "All money transactions related to the house hold."
        );
    }
}
