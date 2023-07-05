package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper mapper;

    Category cat1;
    Category cat2;
    CategoryDto catDto1;

    @BeforeEach
    public void init() {

        cat1 = Category.builder()
                .categoryId("abccd")
                .title("Title 1")
                .description("Desc 1")
                .coverImage("cat1.jpg")
                .products(List.of())
                .build();
        cat2 = Category.builder()
                .categoryId("xyz")
                .title("Title 2")
                .description("Desc 2")
                .coverImage("cat2.jpg")
                .products(List.of())
                .build();

        catDto1 = CategoryDto.builder()
                .categoryId("abccd")
                .title("TitleDto 1")
                .description("DescDto 1")
                .coverImage("catDto1.jpg")
                .products(List.of())
                .build();
    }

    @Test
    void createCategoryTest() {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(cat1);
        CategoryDto categoryDto = categoryService.createCategory(mapper.map(cat1, CategoryDto.class));
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals("Title 1", categoryDto.getTitle(), "Title not matched !!");
    }

    @Test
    void updateCategoryTest() {
        String catId = "wert";
        System.out.println("cat1 before :" + cat1.getCoverImage());
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(cat1));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(cat1);
        CategoryDto categoryDto = categoryService.updateCategory(catDto1, catId);
        System.out.println("cat1 after :" + cat1.getCoverImage());
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(catId);
        Assertions.assertEquals("catDto1.jpg", categoryDto.getCoverImage(), "Not updated !!");
    }

    @Test
    void deleteCategoryTest() {
        String catId = "terjfj";
        Mockito.when(categoryRepository.findById(catId)).thenReturn(Optional.of(cat2));
        categoryService.deleteCategory(catId);
       // Mockito.verify(categoryRepository, Mockito.times(1)).delete(cat2);
        Mockito.verify(categoryRepository,Mockito.times(1)).deleteById(catId);
    }

    @Test
    void getCategoriesTest() {
        List<Category> list = Arrays.asList(cat1, cat2);
        PageImpl<Category> page = new PageImpl<>(list);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<CategoryDto> response = categoryService.getCategories(2, 4, "title", "desc");
        Assertions.assertEquals(2, response.getContent().size());
    }

    @Test
    void getCategoryByIdTest() {
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(cat1));
        CategoryDto categoryDto = categoryService.getCategoryById(Mockito.anyString());
        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals("Title 1", categoryDto.getTitle());
    }

    @Test
    void searchByTitleTest() {
        String keyword = "1";
        Mockito.when(categoryRepository.findByTitleContaining(keyword)).thenReturn(List.of(cat1, cat2));
        List<CategoryDto> categoryDtos = categoryService.searchByTitle(keyword);
        Assertions.assertEquals(2, categoryDtos.size());
    }
}