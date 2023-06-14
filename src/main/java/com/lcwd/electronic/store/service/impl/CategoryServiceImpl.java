package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.helper.PageResponseHelper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    /**
     * @param categoryDto
     * @return
     * @author Ankit
     * @implNote This implementation is for save category data
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Initiated dao call for save category data");
        String catId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(catId);
        Category category = this.mapper.map(categoryDto, Category.class);
        Category savedCategory = this.categoryRepository.save(category);
        log.info("Completed dao call for save category data");
        return this.mapper.map(savedCategory, CategoryDto.class);
    }

    /**
     * @param categoryDto
     * @param categoryId
     * @return
     * @author Ankit
     * @implNote This implementation is for update category data
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        log.info("Initiated dao call for update category data having id :{}", categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATE_NOT_FOUND + categoryId));
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCate = this.categoryRepository.save(category);
        log.info("Completed dao call for update category data having id :{}", categoryId);
        return this.mapper.map(updatedCate, CategoryDto.class);
    }

    /**
     * @param categoryId
     * @author Ankit
     * @implNote This implemetation is for delete category
     */
    @Override
    public void deleteCategory(String categoryId) {
        log.info("Initiated dao call for delete category with id :{}", categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATE_NOT_FOUND + categoryId));
        this.categoryRepository.deleteById(categoryId);
        log.info("Completed dao call for delete category with id :{}", category);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This implementation is for get all categories data
     */
    @Override
    public PageableResponse<CategoryDto> getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initialised dao call for get all categories data with pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = this.categoryRepository.findAll(request);
        PageableResponse<CategoryDto> response = PageResponseHelper.getPageableResponse(categoryPage, CategoryDto.class);
//        List<Category> categoryList = categoryPage.getContent();
//        List<CategoryDto> categoryDtos = categoryList.stream().map((category) -> this.mapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        log.info("Completed dao call for get all categories data with pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sort, sortDir);
        return response;
    }

    /**
     * @param categoryId
     * @return
     * @author Ankit
     * @implNote This implementation is for get all categories data
     */
    @Override
    public CategoryDto getCategoryById(String categoryId) {
        log.info("Initialised dao call for get single category with id :{}", categoryId);
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATE_NOT_FOUND + categoryId));
        log.info("Completed dao call for get single category data with id :{}", categoryId);
        return this.mapper.map(category, CategoryDto.class);
    }

    /**
     * @param keyword
     * @return
     * @author
     * @implNote This implementation is for search category
     */
    @Override
    public List<CategoryDto> searchByTitle(String keyword) {
        log.info("Initiated dao call for search category using keyword :{}", keyword);
        List<Category> categories = this.categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtos = categories.stream().map((category) -> this.mapper.map(category, CategoryDto.class)).collect(Collectors.toList());
        log.info("Completed dao call for search category using keyword :{}", keyword);
        return categoryDtos;
    }
}
