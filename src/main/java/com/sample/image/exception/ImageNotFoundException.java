package com.sample.image.exception;

import org.springframework.http.HttpStatus;

public class ImageNotFoundException extends RuntimeException{

    private HttpStatus httpStatus;
    public ImageNotFoundException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
