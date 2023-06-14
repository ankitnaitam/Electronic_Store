package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface CategoryService {

    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all
    PageableResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    //get single category details
    CategoryDto getCategoryById(String categoryId);

    //search
    List<CategoryDto> searchByTitle(String keyword);
}
