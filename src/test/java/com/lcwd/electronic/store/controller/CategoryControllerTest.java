package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired(required = true)
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    private Category cat1;
    private Category cat2;
    private CategoryDto catDto1;

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
    void createCategoryTest() throws Exception {
        //Mocking method  "/api/categories/"+Put+Json
        Mockito.when(categoryService.createCategory(Mockito.any())).thenReturn(mapper.map(cat1, CategoryDto.class));
        //request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(cat1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists());

    }

    @Test
    void updateCategoryTest() throws Exception {
        Mockito.when(categoryService.updateCategory(Mockito.any(), Mockito.anyString())).thenReturn(catDto1);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/categories/" + cat1.getCategoryId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(cat1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists());
    }

    @Test
    void deleteCategoryTest() throws Exception {
        String catId = "abcd123";
        Mockito.doNothing().when(categoryService).deleteCategory(catId);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/categories/" + catId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(AppConstants.CATE_DELETED + catId));
        Mockito.verify(categoryService).deleteCategory(catId);
    }

    @Test
    void getCategoriesTest() throws Exception {
        CategoryDto dto1 = mapper.map(cat1, CategoryDto.class);
        CategoryDto dto2 = mapper.map(cat2, CategoryDto.class);

        PageableResponse<CategoryDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(dto1, dto2, catDto1));

        Mockito.when(categoryService.getCategories(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/categories/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(pageableResponse))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void getCategoryByIdTest() throws Exception {
        String catId = "adfldk23";
        Mockito.when(categoryService.getCategoryById(catId)).thenReturn(mapper.map(cat1, CategoryDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/categories/" + catId)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
        Mockito.verify(categoryService).getCategoryById(catId);
    }

    @Test
    void searchByTitleTest() throws Exception {
        String subTitle = "title";
        Mockito.when(categoryService.searchByTitle(subTitle)).thenReturn(Arrays.asList(catDto1, mapper.map(cat1, CategoryDto.class)));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/categories/search" + subTitle)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void uploadCoverImageTest() {
    }

    @Test
    void serveCoverImageTest() {
    }

    public String convertObjectToJsonString(Object cat) {
        try {
            return new ObjectMapper().writeValueAsString(cat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}