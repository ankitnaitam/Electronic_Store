package com.lcwd.electronic.store.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadApiRequestException extends RuntimeException{

    public BadApiRequestException() {
        super("Bad Request !!");
    }

    public BadApiRequestException(String message) {
        super(message);
    }
}
