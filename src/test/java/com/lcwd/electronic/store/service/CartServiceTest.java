package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @MockBean
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CartItemRepository cartItemRepository;

    private AddItemToCartRequest request;

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
    void addItemsToCartTest() {
        String userId = "abc123";
        String prodId = "abc123";

        //Mocking
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Mockito.when(productRepository.findById(prodId)).thenReturn(Optional.of(prod1));
        Mockito.when(cartRepository.findByUser(user1)).thenReturn(Optional.of(cart1));
        Mockito.when(cartRepository.save(cart1)).thenReturn(cart1);

        CartDto result = cartService.addItemsToCart(userId, item1);

        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        Mockito.verify(productRepository, Mockito.times(1)).findById(prodId);
        Mockito.verify(cartRepository, Mockito.times(1)).findByUser(user1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(prodId, result.getItems().get(1).getProduct().getProductId());
        Assertions.assertEquals(item1.getQuantity(), result.getItems().get(1).getQuantity());
        Assertions.assertEquals((int) (item1.getQuantity() * prod1.getDiscountedPrice()), result.getItems().get(1).getTotalPrice());
    }

    @Test
    void removeItemFromCartTest() {
        String cartItemId="abc234";
    }

    @Test
    void clearCartTest() {
    }

    @Test
    void getCartByUserTest() {
    }
}