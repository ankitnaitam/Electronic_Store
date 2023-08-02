package com.lcwd.electronic.store.controller;

import com.lcwd.electronic.store.dtos.ApiResponse;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.helper.ApiConstants;
import com.lcwd.electronic.store.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order/")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * @param orderRequest
     * @return
     * @author Ankit
     * @apiNote This api is for create order
     */
    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        log.info("Initiated request for save order of user :{}", orderRequest.getUserId());
        OrderDto orderDto = this.orderService.createOrder(orderRequest);
        log.info("Completed request for save order of user :{}", orderRequest.getUserId());
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    /**
     * @param orderId
     * @return
     * @author Ankit
     * @apiNote This api is for remove order
     */
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse> removeOrder(@RequestParam String orderId) {
        log.info("Initiated request for remove order with orderId :{}", orderId);
        this.orderService.removeOrder(orderId);
        ApiResponse response = ApiResponse.builder()
                .message("Order removed successfully from cart !!")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        log.info("Completed request for remove order with orderId :{}", orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param userId
     * @return
     * @author Ankit
     * @apiNote This api is for get orders of particular user
     */
    @GetMapping(ApiConstants.USER_ID)
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId) {
        log.info("Initiated request to get orders of user :{}", userId);
        List<OrderDto> orderDtos = this.orderService.getOrdersOfUser(userId);
        log.info("Completed request to get orders of user :{}", userId);
        return new ResponseEntity<>(orderDtos, HttpStatus.FOUND);
    }

    /**
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     * @author Ankit
     * @implNote This api is for get orders
     */
    @GetMapping()
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        log.info("Initiated request to get orders having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        PageableResponse<OrderDto> orderDtos = this.orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
        log.info("Completed request to get orders having pageNumber :{},pageSize :{},sortBy :{},sortDir :{}", pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(orderDtos, HttpStatus.FOUND);
    }


}
