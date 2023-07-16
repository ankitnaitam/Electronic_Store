package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

    //Add items to cart :
    //case 1: cart for user is not available, we will create the cart and add items
    //case 2: cart available then add items to cart
    CartDto addItemsToCart(String userId, AddItemToCartRequest request);

    //remove items from cart
    void removeItemFromCart(Integer cartItemId);

    //remove all items from cart
    void clearCart(String usetId);

    //fetch cart-items of user
    CartDto getCartByUser(String userId);
}
