package com.example.demo.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SuccessResponse {
    private HttpStatus status = HttpStatus.OK;
    private int code = HttpStatus.OK.value();
    private Object data = Optional.empty();

    public SuccessResponse() {}

    public SuccessResponse(Object data) {
        this.data = data;
    }

    public SuccessResponse(Object data, HttpStatus status) {
        this.data = data;
        this.status = status;
        this.code = status.value();
    }
}
