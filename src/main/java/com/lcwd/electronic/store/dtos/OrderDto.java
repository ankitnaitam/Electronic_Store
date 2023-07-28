package com.lcwd.electronic.store.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT_PAID";
    private Integer orderAmount;
    private String billingAddress;
    private String billingPhoneNo;
    private String billingName;
    private Date orderDate=new Date();
    private Date deliverdDate;
    private UserDto user;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
