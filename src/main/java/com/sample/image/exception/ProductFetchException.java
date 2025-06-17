package com.sample.image.exception;

import org.springframework.http.HttpStatus;

public class ProductFetchException extends Throwable {
    private HttpStatus httpStatus;
    public ProductFetchException(String string, HttpStatus httpStatus) {
        super(string);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
