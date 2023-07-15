package com.lcwd.electronic.store.dtos;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest {
    private String productId;
    private Integer quantity;
}
