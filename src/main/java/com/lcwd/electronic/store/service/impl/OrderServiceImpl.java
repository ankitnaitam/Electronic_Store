package com.lcwd.electronic.store.service.impl;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.*;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.AppConstants;
import com.lcwd.electronic.store.helper.PageResponseHelper;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    private Order order;

    /**
     * @param orderRequest
     * @return
     * @author Ankit
     * @implNote This implementation is for create order
     */
    @Override
    public OrderDto createOrder(CreateOrderRequest orderRequest) {
        log.info("Initiated dao call for save order of user :{}", orderRequest.getUserId());
        String userId = orderRequest.getUserId();
        String cartId = orderRequest.getCartId();

        log.info("Request to find user with userId :{}", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
        log.info("Request to find cart with cartId :{}", cartId);
        Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.CART_NOT_FOUND + cartId));

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <= 0) {
            throw new BadApiRequestException("Invalid numbers of items in cart !!");
        }

        Order order = Order.builder()
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .orderStatus(orderRequest.getOrderStatus())
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderDate(new Date())
                .build();

        //OrderItems amount
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            //CartItem->OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product((cartItem.getProduct()))
                    .totalPrice(cartItem.getTotalPrice())
                    .build();
            order.setOrderAmount(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        cart.getItems().clear();
        cartRepository.save(cart);

        order.setUser(user);

        order.setOrderId(UUID.randomUUID().toString());
        Order savedOrder = orderRepository.save(order);
        log.info("Completed dao call for save order of user :{}", orderRequest.getUserId());
        return this.mapper.map(savedOrder, OrderDto.class);
    }

    /**
     * @param orderId
     * @author Ankit
     * @implNote This implementation is for remove order from cart
     */
    @Override
    public void removeOrder(String orderId) {
        log.info("Initiated dao call for remove order with orderId :{}", orderId);
        Order order1 = this.orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.ORDER_NOT_FOUND + orderId));
        log.info("Completed dao call for remove order with orderId :{}", orderId);
        this.orderRepository.delete(order1);
    }

    /**
     * @param userId
     * @return
     * @author Ankit
     * @implNote This implementation is for get orders of user
     */
    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        log.info("Initiated dao call to get orders of user :{}", userId);
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));
        List<Order> orders = this.orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> this.mapper.map(order, OrderDto.class)).collect(Collectors.toList());
        log.info("Initiated dao call to get orders with user :{}", userId);
        return orderDtos;
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This implementation is for get orders
     */
    @Override
    public PageableResponse<OrderDto> getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        log.info("Initiated dao call to get orders having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> orders = this.orderRepository.findAll(pageable);
        log.info("Completed dao call to get orders having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        return PageResponseHelper.getPageableResponse(orders, OrderDto.class);
    }
}
