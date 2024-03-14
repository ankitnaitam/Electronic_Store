package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    /**
     * @param userId
     * @param request
     * @return
     * @author Ankit
     * @implNote This implementation is for add items to cart
     */
    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest request) {
        log.info("Initiated dao call for add cart-items to cart of user :{}", userId);

        String productId = request.getProductId();
        Integer quantity = request.getQuantity();

        if (quantity <= 0) {
            throw new BadApiRequestException(AppConstants.QUANTITY_NOT_VALID);
        }

        //fetch the user from db
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        //fetch the product
        Product product = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROD_NOT_FOUND + productId));

        //get cart if exist or create new
        Cart cart = null;
        try {
            log.info("Fetched existing cart of user:{}", userId);
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            log.info("Created new cart of user:{}", userId);
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //Perform cart operation
        //if cart items already present then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        //To solve OrphanRemoval issue here we just change the reference below(instead of assigning new list we performed in that exiting list) as List<CartItem> updatedItems = items.....by
        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //items already present in cart
                log.info("Updating item details of product:{}", productId);
                item.setQuantity(quantity);
                item.setTotalPrice((int) (quantity * product.getDiscountedPrice()));
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems); //instead of setting new updated list we performed changes in existing list as above

        //create items
        if (!updated.get()) {
            log.info("Adding item details to cart");
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice((int) (quantity * product.getDiscountedPrice()))
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart savedCart = this.cartRepository.save(cart);
        log.info("Completed dao call for add cart-items to cart of user :{}", userId);
        return this.mapper.map(savedCart, CartDto.class);
    }

    /**
     * @param cartItemId
     * @author Ankit
     * @implNote This implementation is for remove item from cart
     */
    @Override
    public void removeItemFromCart(Integer cartItemId) {
        log.info("Initiated dao call for remove cart-item from cart having cartItemId :{}", cartItemId);
        CartItem cartItem = this.cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_ITEM_NOT_FOUND + cartItemId));
        log.info("Completed dao call for remove cart-item from cart having cartItemId :{}", cartItemId);
        this.cartItemRepository.delete(cartItem);
    }

    /**
     * @param userId
     * @author Ankit
     * @implNote This implementation is for clear cart
     */
    @Override
    public void clearCart(String userId) {
        log.info("Initiated dao call for clear cart of user :{}", userId);
        //fetch user using userId
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        //fetch cart using fetched user
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_NOT_FOUND + userId));
        //cart.getItems().clear();
        //The error you're encountering, java.lang.UnsupportedOperationException,
        // typically occurs when trying to modify a collection that is immutable.
        // Looking at your code, it seems the issue is arising from trying to clear the items of a collection that might be immutable.
        //
        //In your clearCart method, you're trying to clear the items of the cart using cart.getItems().clear();.
        // This suggests that the getItems() method returns an immutable collection, hence the exception.
        // That's why we are providing empty list as below to clear the cart
//        ArrayList<CartItem> newItems = new ArrayList<>();
//        cart.setItems(newItems);
//        System.out.println(cart.getItems());
        cart.getItems().clear();
        System.out.println(cart.getItems());
        this.cartRepository.save(cart);
        log.info("Completed dao call for clear cart of user :{}", userId);
    }

    /**
     * @param userId
     * @return
     * @author Ankit
     * @implNote This implementation is to get cart details of user
     */
    @Override
    public CartDto getCartByUser(String userId) {
        log.info("Initiated dao call for get cart of user:{}", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_NOT_FOUND + userId));
        log.info("Completed dao call for get cart of user:{}", userId);
        return this.mapper.map(cart, CartDto.class);
    }
}
