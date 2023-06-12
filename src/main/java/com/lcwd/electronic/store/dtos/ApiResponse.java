package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private Boolean success;

    private HttpStatus status;
}
