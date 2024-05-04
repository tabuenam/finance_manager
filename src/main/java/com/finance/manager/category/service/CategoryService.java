package com.finance.manager.category.service;

import com.finance.manager.category.database.Category;
import com.finance.manager.category.model.CategoryModel;
import com.finance.manager.category.repository.CategoryRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public void addCategories(final List<CategoryModel> categoryModels) {
        UserEntity authenticatedUser = authenticatedUserService.getAuthenticatedUser();

        List<Category> categories = categoryModels.stream()
                .map(categoryModel -> mapToEntity(categoryModel, authenticatedUser.getId()))
                .toList();

        categoryRepository.saveAll(categories);
    }

    protected Category mapToEntity(final CategoryModel categoryModel, final Long userId) {
        return Category.builder()
                .categoryName(categoryModel.categoryName())
                .description(categoryModel.categoryDescription())
                .userId(userId)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
