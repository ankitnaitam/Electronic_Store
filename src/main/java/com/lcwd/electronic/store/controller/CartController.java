package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponse;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ApiConstants.CART_BASE_URL)
public class CartController {

    @Autowired
    private CartService cartService;

    //add item to cart

    /**
     * @param userId
     * @param request
     * @return
     * @author Ankit
     * @apiNote This api is for add items to cart
     */
    @PostMapping(ApiConstants.USER_ID)
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        log.info("Initiated request for add items to cart of User:{}", userId);
        CartDto cartDto = this.cartService.addItemsToCart(userId, request);
        log.info("Completed request for add items to cart of User:{}", userId);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    //remove item from cart

    /**
     * @param cartItemId
     * @return
     * @author Ankit
     * @apiNote This api is for remove item from cart
     */
    @DeleteMapping(ApiConstants.CART_ITEM_ID)
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Integer cartItemId) {
        log.info("Initiated request for remove item from cart having cartItemId:{}", cartItemId);
        this.cartService.removeItemFromCart(cartItemId);
        ApiResponse response = ApiResponse.builder()
                .message("Cart item removed successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        log.info("Competed request for remove item from cart having cartItemId:{}", cartItemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //clear cart

    /**
     * @param userId
     * @return
     * @author Ankit
     * @apiNote This api is for clear cart
     */
    @DeleteMapping(ApiConstants.USER_ID)
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {
        log.info("Initiated request for clear cart of user:{}", userId);
        this.cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder()
                .message("Cart cleared successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        log.info("Completed request for clear cart of user:{}", userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //get cart

    /**
     * @param userId
     * @return
     * @author Ankit
     * @apiNote This api is for get cart of cart
     */
    @GetMapping(ApiConstants.USER_ID)
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        log.info("Initiated request for get cart of user:{}", userId);
        CartDto cartDto = this.cartService.getCartByUser(userId);
        log.info("Completed request for get cart of user:{}", userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
