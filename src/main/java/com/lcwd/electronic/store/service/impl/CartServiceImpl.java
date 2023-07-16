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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
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

    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest request) {

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
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //Perform cart operation
        //if cart items already present then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        List<CartItem> updatedItems = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //items already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice((int) (quantity * product.getDiscountedPrice()));
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
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
        return this.mapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(Integer cartItemId) {
        CartItem cartItem = this.cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_ITEM_NOT_FOUND + cartItemId));
        this.cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        //fetch user using userId
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        //fetch cart using fetched user
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_NOT_FOUND + userId));
        cart.getItems().clear();
        System.out.println(cart.getItems());
        this.cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        Cart cart = this.cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_NOT_FOUND + userId));
        return this.mapper.map(cart, CartDto.class);
    }
}
