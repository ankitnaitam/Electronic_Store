package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class ProductDto extends CustomFieldsDto {

    private String productId;

    @NotBlank
    @Size(min = 4, max = 30, message = "Title must be of min 4 characters and max 30 characters !!")
    private String productTitle;

    @NotBlank(message = "Description should not be blank !!")
    private String productDescription;

    @NotEmpty
    private Double price;

    @NotEmpty
    private Double discountPrice;

    @NonNull
    private Long quantity;

    private Boolean stock;
}
