package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponse;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request) {
        CartDto cartDto = this.cartService.addItemsToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
    }

    //remove item from cart
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Integer cartItemId) {
        this.cartService.removeItemFromCart(cartItemId);
        ApiResponse response = ApiResponse.builder()
                .message("Cart item removed successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //clear cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable String userId) {
        this.cartService.clearCart(userId);
        ApiResponse response = ApiResponse.builder()
                .message("Cart cleared successfully !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(@PathVariable String userId) {
        CartDto cartDto = this.cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }
}
