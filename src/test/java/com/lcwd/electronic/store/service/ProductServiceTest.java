package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper mapper;

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
                .discountPrice(986.23)
                .quantity(235l)
                .stock(true)
                .productImage("prod1.jpg")
                .category(null)
                .build();
        prod2 = Product.builder()
                .productId("dgag46")
                .productTitle("Prod 2")
                .productDescription("This is prod 2")
                .price(8958.12)
                .discountPrice(586.23)
                .quantity(295l)
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
                .quantity(235l)
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
//        Mockito.verify(productRepository,Mockito.times(1)).deleteById(prod2.getProductId());
//        Mockito.verify(productRepository,Mockito.times(1)).deleteById(prodId);
        Mockito.verify(productRepository, Mockito.times(1)).delete(prod2);
    }

    @Test
    void getProductTest() {
    }

    @Test
    void getProductsTest() {
    }

    @Test
    void getIsLiveProductsTest() {
    }

    @Test
    void searchProductTest() {
    }
}