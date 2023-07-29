package com.lcwd.electronic.store.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {
    private String userId;
    private String cartId;

    private String orderStatus="PENDING";
    private String paymentStatus="NOT-PAID";
    private String billingAddress;
    private String billingPhone;
    private String billingName;
}
