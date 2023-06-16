package com.lcwd.electronic.store.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_details")
public class Product extends CustomFields {

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "product_description", length = 1000)
    private String productDescription;

    private Double price;

    @Column(name = "discount_price")
    private Double discountPrice;

    private Long quantity;

    private Boolean stock;

}
