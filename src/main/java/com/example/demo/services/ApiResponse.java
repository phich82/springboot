package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse {

    public static ResponseEntity<?> error() {
        return toJsonError("Bad Request", HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> error(String message) {
        return toJsonError(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> error(MultiValueMap<String, String> message) {
        return toJsonError(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> error(Map<String, List<String>> message) {
        return toJsonError(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<?> error(String message, HttpStatus status) {
        return toJsonError(message, status);
    }

    public static ResponseEntity<?> error(Map<String, List<String>> message, HttpStatus status) {
        return toJsonError(message, status);
    }

    public static ResponseEntity<?> error(MultiValueMap<String, String> message, HttpStatus status) {
        return toJsonError(message, status);
    }

    public static ResponseEntity<?> success() {
        return toJsonSuccess(Optional.empty(), HttpStatus.OK);
    }

    public static ResponseEntity<?> success(Object data) {
        return toJsonSuccess(data, HttpStatus.OK);
    }

    public static ResponseEntity<?> success(Object data, HttpStatus status) {
        return toJsonSuccess(data, status);
    }

    private static ResponseEntity<?> toJsonError(Object message) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(Map.of(
            "status", status,
            "code", status.value(),
            "message", message
        ));
    }

    private static ResponseEntity<?> toJsonError(Object message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of(
            "status", status,
            "code", status.value(),
            "message", message
        ));
    }

    private static ResponseEntity<?> toJsonSuccess(Object data) {
        HttpStatus status = HttpStatus.OK;
        return ResponseEntity.status(status).body(Map.of(
            "status", status,
            "code", status.value(),
            "data", data
        ));
    }

    private static ResponseEntity<?> toJsonSuccess(Object data, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of(
            "status", status,
            "code", status.value(),
            "data", data
        ));
    }

}
