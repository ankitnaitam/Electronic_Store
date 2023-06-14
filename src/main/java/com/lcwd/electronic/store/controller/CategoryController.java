package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponse;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/categories/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //create
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Initialized request for save category data");
        CategoryDto newCategory = this.categoryService.createCategory(categoryDto);
        log.info("Completed request for save category data");
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        log.info("Initialized request for update category data having id:{}", categoryId);
        CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        log.info("Completed request for update category data having id:{}", categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId) {
        log.info("Initialized request for delete category data with id:{}", categoryId);
        this.categoryService.deleteCategory(categoryId);
        log.info("Completed request for delete category data with id:{}", categoryId);
        return new ResponseEntity<>(AppConstants.CATE_DELETED + categoryId, HttpStatus.OK);
    }

    //get all
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>> getCategories(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.CATE_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
        log.info("Initialised request for get all categories data with pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<CategoryDto> categories = this.categoryService.getCategories(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for get all categories data with pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categories, HttpStatus.FOUND);
    }

    //get single category data
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        log.info("Request for get single category data with id:{}", categoryId);
        return new ResponseEntity<>(this.categoryService.getCategoryById(categoryId), HttpStatus.FOUND);
    }

    //search
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchByTitle(@RequestParam String keyword) {
        log.info("Request for get categories data with keyword:{}", keyword);
        return new ResponseEntity<>(this.categoryService.searchByTitle(keyword), HttpStatus.FOUND);
    }


}
