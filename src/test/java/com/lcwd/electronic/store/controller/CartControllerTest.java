package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.service.CartService;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mapper;

    private User user1;

    private Product prod1;

    private Cart cart1;

    private CartItem cartItem1;
    private CartItem cartItem2;
    private AddItemToCartRequest item1;

    @BeforeEach
    public void init() {
        item1 = AddItemToCartRequest.builder()
                .productId("abc123")
                .quantity(20)
                .build();

        user1 = User.builder()
                .name("Virat")
                .email("viratkohli18@gmail.com")
                .about("This is unit testing")
                .gender("male")
                .imageName("abc.png")
                .password("virat18")
                .build();

        prod1 = Product.builder()
                .productId("abc123")
                .productTitle("Prod 1")
                .productDescription("This is prod 1")
                .price(2333.12)
                .discountedPrice(986.23)
                .quantity(235L)
                .stock(true)
                .productImage("prod1.jpg")
                .category(null)
                .build();

        cartItem1 = CartItem.builder()
                .cartItemId(123)
                .product(prod1)
                .quantity(20)
                .totalPrice(2000)
                .cart(cart1)
                .build();
        cartItem2 = CartItem.builder()
                .cartItemId(456)
                .product(prod1)
                .quantity(5)
                .totalPrice(500)
                .cart(cart1)
                .build();

        cart1 = Cart.builder()
                .cartId("1")
                .createdAt(new Date())
                .user(user1)
                .items(List.of(cartItem1, cartItem2))
                .build();

    }


    @Test
    void addItemToCartTest() throws Exception {
        String userId = "add123";
        // Post+"/api/cart/{userId}"+data as Json+status CREATED
        Mockito.when(cartService.addItemsToCart(userId, item1)).thenReturn(mapper.map(cart1, CartDto.class));
        // Actual request for url
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/cart/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(cart1))
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartId").exists());
    }

    @Test
    void removeItemFromCartTest() {
    }

    @Test
    void clearCartTest() {
    }

    @Test
    void getCartByUserTest() {
    }

    private String convertObjectToJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}