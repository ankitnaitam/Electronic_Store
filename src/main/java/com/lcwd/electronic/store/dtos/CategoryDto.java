package com.lcwd.electronic.store.dtos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Size(min = 4,max = 30,message = "Title must be of min 4 characters and max 30 characters !!")
    private String title;

    @NotBlank(message = "Description should not be blank !!")
    private String description;

    @NotBlank
    private String coverImage;
}
