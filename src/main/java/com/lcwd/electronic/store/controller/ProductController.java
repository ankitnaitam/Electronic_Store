package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.FileService;
import com.lcwd.electronic.store.service.ProductService;
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

@RestController
@Slf4j
@RequestMapping(ApiConstants.PROD_BASE_URL)
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Value("${product.profile.image.path}")
    private String productImageUploadPath;

    //create

    /**
     * @param productDto
     * @return
     * @author Ankit
     * @apiNote This api is for save product details
     */
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Initiated request for save product details");
        ProductDto newProduct = this.productService.createProduct(productDto);
        log.info("Completed request for save product details");
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    //update

    /**
     * @param productDto
     * @param productId
     * @return
     * @author Ankit
     * @apiNote This api is for update product details
     */
    @PutMapping(ApiConstants.PROD_ID)
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable String productId) {
        log.info("Initiated request for update product details with id :{}", productId);
        ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
        log.info("Completed request for update product details with id:{}", productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    //delete

    /**
     * @param productId
     * @return
     * @author Ankit
     * @apiNote This api is for delete product details
     */
    @DeleteMapping(ApiConstants.PROD_ID)
    public ResponseEntity<String> deleteProduct(@PathVariable String productId) {
        log.info("Initiated request for delete product with id:{}", productId);
        this.productService.deleteProduct(productId);
        log.info("Completed request for delete product with id:{}", productId);
        return new ResponseEntity<>(AppConstants.PROD_DELETED + productId, HttpStatus.OK);
    }

    //get single

    /**
     * @param productId
     * @return
     * @author Ankit
     * @apiNote This api is for get single product details
     */
    @GetMapping(ApiConstants.PROD_ID)
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
        ProductDto product = this.productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }

    //get all

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @apiNote This api is for get all products
     */
    @GetMapping()
    public ResponseEntity<PageableResponse<ProductDto>> getProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "price", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("Initiated request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<ProductDto> products = this.productService.getProducts(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.FOUND);
    }

    //get all:isLive

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @apiNote This api is for get live products
     */
    @GetMapping(ApiConstants.PROD_IS_LIVE)
    public ResponseEntity<PageableResponse<ProductDto>> getIsLiveProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "price", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("Initiated request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<ProductDto> products = this.productService.getIsLiveProducts(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.FOUND);
    }

    //search

    /**
     * @param subTitle
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @apiNote This api is for search product using keyword
     */
    @GetMapping(ApiConstants.PROD_SEARCH)
    public ResponseEntity<PageableResponse<ProductDto>> searchProducts(
            @PathVariable String subTitle,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "price", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("Initiated request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<ProductDto> products = this.productService.searchProduct(subTitle, pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request for get all product details having pageNumber:{},pageSize:{},sortBy:{},sortDir:{}", pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(products, HttpStatus.FOUND);
    }

    /**
     * @param image
     * @param productId
     * @return
     * @throws IOException
     * @author Ankit
     * @apiNote This api is for upload product image
     */
    @PostMapping(ApiConstants.PROD_IMG)
    public ResponseEntity<ImageResponse> uploadProductImage(
            @RequestParam("productImage") MultipartFile image,
            @PathVariable String productId) throws IOException {
        log.info("Initiated request for uploading product image with id :{}", productId);
        String imageName = this.fileService.uploadFile(image, productImageUploadPath);
        //to update image name
        ProductDto product = this.productService.getProduct(productId);
        product.setProductImage(imageName);
        ProductDto updatedProduct = this.productService.updateProduct(product, productId);
        //image response
        ImageResponse response = ImageResponse.builder().imageName(imageName).message(AppConstants.IMAGE_UPLOADED).success(true).status(HttpStatus.CREATED).build();
        log.info("Initiated request for uploading product image with id :{}", productId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @param productId
     * @param response
     * @throws IOException
     * @author Ankit
     * @apiNote This api is for serve product image
     */
    @GetMapping(ApiConstants.PROD_IMG)
    public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        log.info("Initiated request for serve product image with id :{}", productId);
        ProductDto productDto = this.productService.getProduct(productId);
        log.info("Cover Image name:{}", productDto.getProductImage());
        InputStream resource = this.fileService.getResource(productImageUploadPath, productDto.getProductImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
        log.info("Initiated request for serve product image with id :{}", productId);
    }
}
