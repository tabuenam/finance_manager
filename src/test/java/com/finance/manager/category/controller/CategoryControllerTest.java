package com.finance.manager.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.manager.category.model.CategoryModel;
import com.finance.manager.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.finance.manager.category.CategoryTestData.buildCategoryModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @MockBean
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private CategoryModel categoryModel;

    @BeforeEach
    void setUp() {
        categoryModel = buildCategoryModel();
    }

    @Test
    void itShouldCreateNewCategories() throws Exception {
        //Arrange
        doNothing().when(categoryService)
                .addCategories(any());
        //Act
        ResultActions resultActions =
                mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(categoryModel)))
                        .with(SecurityMockMvcRequestPostProcessors.jwt())
                        .characterEncoding("UTF-8")
                );
        //Assert
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

//                .andExpect(jsonPath("$.userName", is(userAccountDetailModel.userName())))
//                .andExpect(jsonPath("$.email", is(userAccountDetailModel.email())));

        verify(categoryService, times(1))
                .addCategories(any());
    }
}