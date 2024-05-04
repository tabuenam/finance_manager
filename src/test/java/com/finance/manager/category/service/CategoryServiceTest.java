package com.finance.manager.category.service;

import com.finance.manager.category.database.Category;
import com.finance.manager.category.model.CategoryModel;
import com.finance.manager.category.repository.CategoryRepository;
import com.finance.manager.user.database.UserEntity;
import com.finance.manager.user.services.impl.AuthenticatedUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.finance.manager.category.CategoryTestData.buildCategoryModel;
import static com.finance.manager.user.services.impl.UserServiceImplTestData.buildUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AuthenticatedUserService authenticatedUserService;
    @InjectMocks
    private CategoryService underTest;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = buildUserEntity();
    }

    @Test
    void itShouldMapToACategoryEntityWithCategoryModelAndUserEntity() {
        //Arrange
        UserEntity userEntity = buildUserEntity();
        CategoryModel categoryModel = buildCategoryModel();
        //Act
        Category category = underTest.mapToEntity(categoryModel, userEntity.getId());
        //Assert
        assertNotNull(category);
        assertEquals(categoryModel.categoryDescription(), category.getDescription());
        assertEquals(categoryModel.categoryName(), category.getCategoryName());
        assertEquals(userEntity.getId(), category.getUserId());
    }

    @Test
    void itShouldPersistNewCategoriesToTheDb() {
        //Arrange
        when(authenticatedUserService.getAuthenticatedUser())
                .thenReturn(userEntity);
        List<CategoryModel> categoryModels =
                List.of(buildCategoryModel());
        //Act
        underTest.addCategories(categoryModels);

        //Assert
        verify(authenticatedUserService, times(1))
                .getAuthenticatedUser();

        verify(categoryRepository, times(1))
                .saveAll(anyIterable());
    }
}