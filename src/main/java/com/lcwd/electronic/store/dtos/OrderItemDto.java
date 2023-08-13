package com.lcwd.electronic.store.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Integer orderItemId;
    private Integer quantity;
    private Integer totalPrice;
    private ProductDto product;

}
