package com.sample.image.exception;

import org.springframework.http.HttpStatus;

public class ImageDataAccessException extends RuntimeException {
    private HttpStatus httpStatus;
    public ImageDataAccessException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
