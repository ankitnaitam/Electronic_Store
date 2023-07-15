package com.lcwd.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
