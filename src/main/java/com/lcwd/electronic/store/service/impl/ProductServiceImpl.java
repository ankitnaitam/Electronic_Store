package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.helper.PageResponseHelper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.profile.image.path}")
    private String productImagePath;

    /**
     * @param productDto
     * @return
     * @author Ankit
     * @implNote This implementation is for save product details
     */
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        log.info("Initialized dao call for save product details");
        String id = UUID.randomUUID().toString();
        productDto.setProductId(id);
        Product product = this.mapper.map(productDto, Product.class);
        Product savedProduct = this.productRepo.save(product);
        log.info("Completed dao call for save product details");
        return this.mapper.map(savedProduct, ProductDto.class);
    }

    /**
     * @param productDto
     * @param productId
     * @return
     * @author Ankit
     * @implNote This implementation is for update product details
     */
    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        log.info("Initialized dao call for update product details with id :{}", productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROD_NOT_FOUND + productId));
        product.setProductTitle(productDto.getProductTitle());
        product.setProductDescription(productDto.getProductDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(productDto.getStock());
        product.setIsActive(productDto.getIsActive());
        product.setModifiedBy(productDto.getModifiedBy());
        product.setProductImage(productDto.getProductImage());
        Product updatedProduct = this.productRepo.save(product);
        log.info("Completed dao call for update product details with id :{}", productId);
        return this.mapper.map(updatedProduct, ProductDto.class);
    }

    /**
     * @param productId
     * @author Ankit
     * @implNote This implementation is for delete product details
     */
    @Override
    public void deleteProduct(String productId) {
        log.info("Initialized dao call for delete product details with id :{}", productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROD_NOT_FOUND + productId));
        this.productRepo.delete(product);
        log.info("Completed dao call for delete product details with id :{}", productId);
    }

    /**
     * @param productId
     * @return
     * @author Ankit
     * @implNote This implementation is for get single product details
     */
    @Override
    public ProductDto getProduct(String productId) {
        log.info("Completed dao call for update product details with id :{}", productId);
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROD_NOT_FOUND + productId));
        log.info("Completed dao call for update product details with id :{}", productId);
        return this.mapper.map(product, ProductDto.class);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This implementation is for get all product details
     */
    @Override
    public PageableResponse<ProductDto> getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initialized dao call for get all product details having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = this.productRepo.findAll(request);
        PageableResponse<ProductDto> response = PageResponseHelper.getPageableResponse(productPage, ProductDto.class);
        log.info("Completed dao call for get all product details having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        return response;
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This implementation is for get all live product details
     */
    @Override
    public PageableResponse<ProductDto> getIsLiveProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initialized dao call for get all live product details having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = this.productRepo.findByIsActiveTrue(request);
        PageableResponse<ProductDto> response = PageResponseHelper.getPageableResponse(productPage, ProductDto.class);
        log.info("Completed dao call for get all live product details having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        return response;
    }


    /**
     * @param subTitle
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This implementation is for search products containing keyword
     */
    @Override
    public PageableResponse<ProductDto> searchProduct(String subTitle, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initialized dao call for search product details having subTitle :{},pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", subTitle, pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = (Page<Product>) this.productRepo.findByProductTitleContaining(subTitle, request);
        PageableResponse<ProductDto> response = PageResponseHelper.getPageableResponse(productPage, ProductDto.class);
        log.info("Completed dao call for search product details having subTitle :{},pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", subTitle, pageNumber, pageSize, sortBy, sortDir);
        return response;
    }

    /**
     * @param productDto
     * @param categoryId
     * @return
     * @author Ankit
     * @implNote This implementation is for save product with category
     */
    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        log.info("Initialized dao call for save product details with categoryId :{}", categoryId);
        //fetch the category from db
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATE_NOT_FOUND + categoryId));
        //product
        String id = UUID.randomUUID().toString();
        productDto.setProductId(id);
        Product product = this.mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = this.productRepo.save(product);
        log.info("Completed dao call for save product details with categoryId :{}", categoryId);
        return this.mapper.map(savedProduct, ProductDto.class);
    }

    /**
     * @param productId
     * @param categoryId
     * @return
     * @author Ankit
     * @implNote This implementation is to assign category to product
     */
    @Override // In which order we fetch object(category,product) from db as below in same order we have to provide arguments otherwise it will throw RNFE as not found with catId: prodId
    public ProductDto assignCategoryToProduct(String categoryId, String productId) {
        log.info("Initialized dao call to assign category with categoryId :{} to existing product with productId :{}", categoryId, productId);
        //fetch category form db
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CATE_NOT_FOUND + categoryId));
        //fetch product from dbA
        Product product = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROD_NOT_FOUND + productId));
        //assign category to existing product
        product.setCategory(category);
        Product savedProduct = this.productRepo.save(product);
        log.info("Completed dao call to assign category with categoryId :{} to existing product with productId :{}", categoryId, productId);
        return this.mapper.map(savedProduct, ProductDto.class);
    }
}
