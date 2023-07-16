package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private CategoryRepository categoryRepository;

    private Product prod1;
    private Product prod2;
    private ProductDto prodDto1;

    @BeforeEach
    public void init() {
        prod1 = Product.builder()
                .productId("asd123")
                .productTitle("Prod 1")
                .productDescription("This is prod 1")
                .price(2333.12)
                .discountedPrice(986.23)
                .quantity(235L)
                .stock(true)
                .productImage("prod1.jpg")
                .category(null)
                .build();
        prod2 = Product.builder()
                .productId("dgag46")
                .productTitle("Prod 2")
                .productDescription("This is prod 2")
                .price(8958.12)
                .discountedPrice(586.23)
                .quantity(295L)
                .stock(true)
                .productImage("prod2.jpg")
                .category(null)
                .build();

        prodDto1 = ProductDto.builder()
                .productId("asd123")
                .productTitle("ProdDto 1")
                .productDescription("This is prodDto 1")
                .price(8953.12)
                .discountPrice(856.23)
                .quantity(235L)
                .stock(true)
                .productImage("prod1.jpg")
                .category(null)
                .build();
    }

    @Test
    void createProductTest() {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(prod1);
        ProductDto productDto = productService.createProduct(mapper.map(prod1, ProductDto.class));
        Assertions.assertNotNull(productDto);
        Assertions.assertEquals("Prod 1", productDto.getProductTitle(), "Title not matched !!");
    }

    @Test
    void updateProductTest() {
        String prodId = "wert123";
        System.out.println("price before updating :" + prod1.getPrice());
        Mockito.when(productRepository.findById(prodId)).thenReturn(Optional.of(prod1));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(prod1);
        ProductDto productDto = productService.updateProduct(prodDto1, prodId);
        System.out.println("price after updating :" + prod1.getPrice());
        Mockito.verify(productRepository, Mockito.times(1)).findById(prodId);
        Assertions.assertEquals(8953.12, productDto.getPrice(), "Price not matched !!");
    }

    @Test
    void deleteProductTest() {
        String prodId = "we234";
        Mockito.when(productRepository.findById(prodId)).thenReturn(Optional.of(prod2));
        productService.deleteProduct(prodId);
        Mockito.verify(productRepository, Mockito.times(1)).delete(prod2);
    }

    @Test
    void getProductTest() {
        String prodId = "thfiek234";
        Mockito.when(productRepository.findById(prodId)).thenReturn(Optional.of(prod1));
        ProductDto productDto = productService.getProduct(prodId);
        Assertions.assertNotNull(productDto);
        Assertions.assertEquals("Prod 1", productDto.getProductTitle(), "Title not matched !!");
    }

    @Test
    void getProductsTest() {
        Product prod3 = Product.builder()
                .productId("hpfjj433")
                .productTitle("Prod 3")
                .productDescription("This is prod 3")
                .price(45658.12)
                .discountedPrice(8996.23)
                .quantity(985L)
                .stock(true)
                .productImage("prod3.jpg")
                .category(null)
                .build();
        List<Product> list = Arrays.asList(prod1, prod2, prod3);
        PageImpl<Product> page = new PageImpl<>(list);
        Mockito.when(productRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<ProductDto> response = productService.getProducts(1, 3, "productTitle", "asc");
        Assertions.assertEquals(3, response.getContent().size(), "Number of product are not same as expected");
    }

    @Test
    void createWithCategoryTest() {
        // String catId="dshjfa34";
        Category cat1 = Category.builder()
                .categoryId("dshjfa34")
                .title("Title 1")
                .description("Desc 1")
                .coverImage("cat1.jpg")
                .build();

        Mockito.when(categoryRepository.findById(cat1.getCategoryId())).thenReturn(Optional.of(cat1));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(prod1);

        ProductDto productDto = productService.createWithCategory(mapper.map(prod1, ProductDto.class), cat1.getCategoryId());

        System.out.println(productDto.getCategory()); //NullPointerException if .getTitle
        System.out.println(productDto.toString());

        Assertions.assertNotNull(productDto);
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(cat1.getCategoryId());
    }

    @Test
    void assignCategoryToProductTest() {
        Category cat1 = Category.builder()
                .categoryId("dshjfa34")
                .title("Title 1")
                .description("Desc 1")
                .coverImage("cat1.jpg")
                .build();

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(cat1));
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(prod1));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(prod1);

        ProductDto productDto = productService.assignCategoryToProduct(cat1.getCategoryId(), prod1.getProductId());

        Assertions.assertNotNull(productDto);
        Assertions.assertEquals("Prod 1", productDto.getProductTitle(), "Product title not matched !!");
        Assertions.assertEquals("Title 1", productDto.getCategory().getTitle(), "Category title not matched !!");

        System.out.println(productDto.getCategory().getTitle()); //successfully get

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(cat1.getCategoryId());
        Mockito.verify(productRepository, Mockito.times(1)).findById(prod1.getProductId());
    }

    @Test
    void getIsLiveProductsTest() {
    }

    @Test
    void searchProductTest() {
//        String subTitle = "Prod";
//        Sort sort = Sort.by(Sort.Direction.ASC, ("productTitle"));
//        Pageable pageable = PageRequest.of(0, 2, sort);
//
//        Page<Product> productPage = Mockito.mock(Page.class);
//        PageableResponse<ProductDto> expectedResponse = new PageableResponse<>();
//
//
//        Mockito.when(productRepository.findByProductTitleContaining(subTitle, pageable)).thenReturn(productPage);
//
//        Mockito.when(productRepository.findByProductTitleContaining(subTitle, pageable)).thenReturn(productPage);
//        Mockito.when(mapper.map(Mockito.any(Product.class), Mockito.eq(ProductDto.class))).thenReturn(new ProductDto());
//        Mockito.when(mapper.map(productPage, PageableResponse.class)).thenReturn(expectedResponse);
//
//        // Act
//        PageableResponse<ProductDto> result = productService.searchProduct(subTitle, 0, 3, "productTitle", "asc");
//
//        // Assert
//        assertEquals(expectedResponse, result);
//        Mockito.verify(productRepository, Mockito.times(1)).findByProductTitleContaining(subTitle, pageable);
//        Mockito.verify(mapper, Mockito.times(1)).map(Mockito.any(Product.class), Mockito.eq(ProductDto.class));
//        Mockito.verify(mapper, Mockito.times(1)).map(productPage, PageableResponse.class);
    }
}