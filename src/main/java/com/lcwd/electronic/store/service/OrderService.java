package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    //place order
    OrderDto createOrder(CreateOrderRequest orderRequest);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
