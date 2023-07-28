package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Product;
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
    private Product product;

}
