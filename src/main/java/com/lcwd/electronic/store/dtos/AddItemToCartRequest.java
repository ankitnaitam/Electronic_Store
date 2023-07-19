package com.lcwd.electronic.store.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartRequest {
    private String productId;
    private Integer quantity;
}
