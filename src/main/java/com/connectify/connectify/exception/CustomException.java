package com.connectify.connectify.exception;

import com.connectify.connectify.enums.EError;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final EError error;

    public CustomException(EError error) {
        super(error.getMessage());
        this.error = error;
    }

}
