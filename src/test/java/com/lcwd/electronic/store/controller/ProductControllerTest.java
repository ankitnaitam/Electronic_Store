package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    private Product prod1;
    private Product prod2;
    private ProductDto prodDto1;

    @BeforeEach
    public void init() {
        prod1 = Product.builder()
                .productId("asd123")
                .productTitle("Prod")
                .productDescription("This is prod 1")
                .price(2333.12)
                .discountPrice(986.23)
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
                .discountPrice(586.23)
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
                .stock(true).productImage("prod1.jpg")
                .category(null)
                .build();
    }

    @Test
    void createProductTest() throws Exception {
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(mapper.map(prod1, ProductDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/products/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(prod1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTitle").exists());
    }

    @Test
    void updateProductTest() throws Exception {
        String prodId = "ajlf45";
        Mockito.when(productService.updateProduct(Mockito.any(), Mockito.anyString())).thenReturn(prodDto1);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/products/" + prodId)  //404/405
                                .contentType(MediaType.APPLICATION_JSON) //415
                                .content(convertObjectToJsonString(prod1)) //400
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteProductTest() throws Exception {
        String prodId = "ghujeh899";
        Mockito.doNothing().when(productService).deleteProduct(prodId);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/products/" + prodId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(AppConstants.PROD_DELETED + prodId));
        Mockito.verify(productService).deleteProduct(prodId);
    }

    @Test
    void getProductTest() throws Exception {
        String prodId = "togkfkr554";
        Mockito.when(productService.getProduct(prodId)).thenReturn(mapper.map(prod2, ProductDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/" + prodId)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void getProductsTest() throws Exception {
        ProductDto dto1 = mapper.map(prod1, ProductDto.class);
        ProductDto dto2 = mapper.map(prod2, ProductDto.class);

        PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(dto1, dto2, prodDto1));

        Mockito.when(productService.getProducts(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/")
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void getIsLiveProductsTest() {
    }

    @Test
    void searchProductsTest() throws Exception {
        String subTitle = "prod";
        ProductDto dto1 = mapper.map(prod1, ProductDto.class);
        ProductDto dto2 = mapper.map(prod2, ProductDto.class);

        PageableResponse<ProductDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(dto1, dto2, prodDto1));

        Mockito.when(productService.searchProduct(subTitle, 0, 2, "productTitle", "asc")).thenReturn(pageableResponse);
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/products/search/" + subTitle)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    void assignCategoryToProductTest() throws Exception {
        String catId = "eirjdie33";
        String prodId = "iekdmdke678";

        Mockito.when(productService.assignCategoryToProduct(catId, prodId)).thenReturn(mapper.map(prod2, ProductDto.class));
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/products/category/{categoryId}/product/" + catId, prodId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(prod2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createWithCategoryTest(){
    }

    @Test
    void uploadProductImageTest() {
    }

    @Test
    void serveProductImageTest() {
    }

    public String convertObjectToJsonString(Object prod) {
        try {
            return new ObjectMapper().writeValueAsString(prod);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}