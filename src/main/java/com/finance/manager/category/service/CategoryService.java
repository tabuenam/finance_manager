package com.finance.manager.category.service;

import com.finance.manager.category.database.Category;
import com.finance.manager.category.model.CategoryModel;
import com.finance.manager.category.repository.CategoryRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public void addCategories(final List<CategoryModel> categoryModels) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findByUserMail(authentication.getName());

        List<Category> categories = categoryModels.stream()
                .map(categoryModel -> mapToEntity(categoryModel, user))
                .toList();

        categoryRepository.saveAll(categories);
    }

    private Category mapToEntity(final CategoryModel categoryModel, final UserEntity user) {
        return Category.builder()
                .categoryName(categoryModel.categoryName())
                .description(categoryModel.categoryDescription())
                .userId(user.getId())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
