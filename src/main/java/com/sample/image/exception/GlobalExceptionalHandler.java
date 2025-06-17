package com.sample.image.exception;


import com.sample.image.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionalHandler {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> ProductNotFoundExceptionHandler(ProductNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(),e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> ImageNotFoundExceptionHandler(ImageNotFoundException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ProductFetchException.class)
    public ResponseEntity<ErrorResponse> ProductFetchExceptionHandler(ProductFetchException e){
        ErrorResponse errorResponse = new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }


}
