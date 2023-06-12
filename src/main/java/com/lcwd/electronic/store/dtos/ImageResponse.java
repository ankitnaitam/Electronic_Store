package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private String imageName;

    private String message;

    private Boolean success;

    private HttpStatus status;
}
