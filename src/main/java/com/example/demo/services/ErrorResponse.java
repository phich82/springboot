package com.example.demo.services;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    private int code = HttpStatus.BAD_REQUEST.value();
    private Object message = "Bad Request";

    public ErrorResponse() {}

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Map<String, List<String>> message) {
        this.message = message;
    }

    public ErrorResponse(MultiValueMap<String, String> message) {
        this.message = message;
    }

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public ErrorResponse(Map<String, List<String>> message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public ErrorResponse(MultiValueMap<String, String> message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }

    public ErrorResponse(Object message) {
        this.message = message;
    }

    public ErrorResponse(Object message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.code = status.value();
    }
}
