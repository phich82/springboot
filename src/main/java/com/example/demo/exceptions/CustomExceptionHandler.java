package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.services.ApiResponse;

/* @ControllerAdvice - Can response to a view instead a json form */
// @ControllerAdvice
/* End  - @ControllerAdvice */
@RestControllerAdvice
public class CustomExceptionHandler {
    // @ExceptionHandler(NotFoundException.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    // public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e, WebRequest req) {
    //     // Log err

    // // Map<String, List<String>> message = new HashMap<String, List<String>>();
    // // message.put("mykey1", Arrays.asList("error1"));
    // // message.put("mykey2", Arrays.asList("error2"));
    // // return ApiResponse.error(message);
    //     return ApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND);
    // }

    // @ExceptionHandler(BadRequestException.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // public ResponseEntity<?> badRequestExceptionHandler(BadRequestException e, WebRequest req) {
    //     // Log err

    //     return ApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST);
    // }

    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // public ResponseEntity<?> exceptionHandler(Exception e, WebRequest req) {
    //     // Log err

    //     return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
