package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "order_item")
public class OrderItem {
    @Id
    private Integer orderItemId;
    private Integer quantity;
    private Integer totalPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;
}
