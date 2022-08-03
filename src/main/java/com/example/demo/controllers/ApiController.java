package com.example.demo.controllers;

import org.springframework.http.HttpStatus;

import com.example.demo.services.ErrorResponse;
import com.example.demo.services.SuccessResponse;


public abstract class ApiController {

    public SuccessResponse successResponse() {
        return new SuccessResponse();
    }

    public SuccessResponse successResponse(Object data) {
        return new SuccessResponse(data);
    }

    public SuccessResponse successResponse(Object data, HttpStatus status) {
        return new SuccessResponse(data, status);
    }

    public ErrorResponse errorResponse() {
        return new ErrorResponse();
    }

    public ErrorResponse errorResponse(Object message) {
        return new ErrorResponse(message);
    }

    public ErrorResponse errorResponse(Object message, HttpStatus status) {
        return new ErrorResponse(message, status);
    }

}
