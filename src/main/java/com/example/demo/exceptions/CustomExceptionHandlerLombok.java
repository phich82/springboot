package com.example.demo.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import org.springframework.validation.FieldError;

import com.example.demo.services.ErrorResponse;

/* @ControllerAdvice - Can response to a view instead a json form */
// @ControllerAdvice
/* End  - @ControllerAdvice */
@RestControllerAdvice
public class CustomExceptionHandlerLombok {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(NotFoundException e, WebRequest req) {
        // Log err

        // Map<String, List<String>> message = new HashMap<String, List<String>>();
        // message.put("mykey1", Arrays.asList("error1"));
        // message.put("mykey2", Arrays.asList("error2"));
        // return new ErrorResponse(message);
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceptionHandler(BadRequestException e, WebRequest req) {
        // Log err

        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Specify which exception this handler can handle
    @ExceptionHandler(BindException.class)
    // Specify the HTTP Status code to return when an error occurs
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // a ValidationError object for each field that did not pass validation.
    @ResponseBody
    public ErrorResponse validationExceptionHandler(BindException e, WebRequest req) {
        // Log err

        // Map<String, String> errors = new HashMap<>();
        Map<String, List<String>> errors = new HashMap<String, List<String>>();
        // Get the Binding Result and all field errors
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        // Store error fields and its error messages
        for (FieldError currentError: fieldErrors) {
            // errors.put(currentError.getField(), currentError.getDefaultMessage());

            String currentField = currentError.getField();
            String currentMessage = currentError.getDefaultMessage();
            List<String> messages = new ArrayList<String>();

            if (errors.containsKey(currentField)) {
                messages = errors.get(currentField);
            }
            messages.add(currentMessage);
            errors.put(currentField, messages);
        }

        return new ErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(Exception e, WebRequest req) {
        // Log err

        System.out.println("Exception => " + e);
        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
