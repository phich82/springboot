package com.example.demo.validations.rules;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.services.Helper;


public class MinValidator implements ConstraintValidator<Min, Object>
{
    private long minValue;
    private String[] onlyfor;

    public void initialize(Min constraintAnnotation) {
        this.minValue = constraintAnnotation.value();
        this.onlyfor = constraintAnnotation.onlyfor();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cxt) {
        if (Arrays.asList(this.onlyfor).contains("numeric") && Helper.isNumeric(value)) {
            return (long) value >= this.minValue;
        }
        if (value instanceof String) {
            return ((String) value).length() >= this.minValue;
        }
        if (value instanceof List) {
            return ((List<?>) value).size() >= this.minValue;
        }
        return value == null;
    }
}
