package com.example.demo.exceptions;

import java.util.HashMap;

public class AutoValidationException extends RuntimeException {

    public Object errors = new HashMap<String, Object>();

    public AutoValidationException(String message, Object... errors) {
        super(message);
        if (errors.length > 0) {
            this.errors = errors[0];
        }
    }
}
