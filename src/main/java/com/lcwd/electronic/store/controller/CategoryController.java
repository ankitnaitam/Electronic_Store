package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.CategoryService;
import com.lcwd.electronic.store.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConstants.CATE_BASE_URL)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Value("${category.profile.image.path}")
    private String coverImageUploadPath;

    //create

    /**
     * @param categoryDto
     * @return
     * @author Ankit
     * @apiNote This api is for creating category data
     */
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Initialized request for save category data");
        CategoryDto newCategory = this.categoryService.createCategory(categoryDto);
        log.info("Completed request for save category data");
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    //update

    /**
     * @param categoryDto
     * @param categoryId
     * @return
     * @author Ankit
     * @apiNote This api is for update category data
     */
    @PutMapping(ApiConstants.CATE_ID)
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId) {
        log.info("Initialized request for update category data having id:{}", categoryId);
        CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, categoryId);
        log.info("Completed request for update category data having id:{}", categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.CREATED);
    }

    //delete

    /**
     * @param categoryId
     * @return
     * @author Ankit
     * @apiNote This api is for delete category data
     */
    @DeleteMapping(ApiConstants.CATE_ID)
    public ResponseEntity<String> deleteCategory(@PathVariable String categoryId) {
        log.info("Initialized request for delete category data with id:{}", categoryId);
        this.categoryService.deleteCategory(categoryId);
        log.info("Completed request for delete category data with id:{}", categoryId);
        return new ResponseEntity<>(AppConstants.CATE_DELETED + categoryId, HttpStatus.OK);
    }

    //get all

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @apiNote This api is for get categories
     */
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

    /**
     * @param categoryId
     * @return
     * @author Ankit
     * @apiNote This api is for get single category data
     */
    @GetMapping(ApiConstants.CATE_ID)
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId) {
        log.info("Request for get single category data with id:{}", categoryId);
        return new ResponseEntity<>(this.categoryService.getCategoryById(categoryId), HttpStatus.FOUND);
    }

    //search

    /**
     * @param keyword
     * @return
     * @author Ankit
     * @apiNote This api is for search category
     */
    @GetMapping(ApiConstants.CATE_SEARCH)
    public ResponseEntity<List<CategoryDto>> searchByTitle(@RequestParam String keyword) {
        log.info("Request for get categories data with keyword:{}", keyword);
        return new ResponseEntity<>(this.categoryService.searchByTitle(keyword), HttpStatus.FOUND);
    }

    /**
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     * @author Ankit
     * @apiNote This api is for upload cover image
     */
    @PostMapping(ApiConstants.CATE_IMG)
    public ResponseEntity<ImageResponse> uploadCoverImage(
            @RequestParam("coverImage") MultipartFile image,
            @PathVariable String categoryId) throws IOException {
        log.info("Initiated request for uploading category cover image with id :{}", categoryId);
        String imageName = this.fileService.uploadFile(image, coverImageUploadPath);
        //to update image name
        CategoryDto category = this.categoryService.getCategoryById(categoryId);
        category.setCoverImage(imageName);
        CategoryDto categoryDto = this.categoryService.updateCategory(category, categoryId);
        //image response
        ImageResponse response = ImageResponse.builder().imageName(imageName).message(AppConstants.IMAGE_UPLOADED).success(true).status(HttpStatus.CREATED).build();
        log.info("Initiated request for uploading category cover image with id :{}", categoryId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @param categoryId
     * @param response
     * @throws IOException
     * @author Ankit
     * @apiNote This api is for serve cover image
     */
    @GetMapping(ApiConstants.CATE_IMG)
    public void serveCoverImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        log.info("Initiated request for serve category cover image with id :{}", categoryId);
        CategoryDto categoryDto = this.categoryService.getCategoryById(categoryId);
        log.info("Cover Image name:{}", categoryDto.getCoverImage());
        InputStream resource = this.fileService.getResource(coverImageUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Initiated request for serve category cover image with id :{}", categoryId);
    }
}
