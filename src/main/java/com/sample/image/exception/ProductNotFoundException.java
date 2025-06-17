package com.sample.image.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends RuntimeException {
    private HttpStatus httpStatus;
    public ProductNotFoundException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
