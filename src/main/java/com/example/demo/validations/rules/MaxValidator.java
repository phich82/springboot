package com.example.demo.validations.rules;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.demo.services.Helper;


public class MaxValidator implements ConstraintValidator<Max, Object>
{
    private long maxValue;
    private String[] onlyfor;

    public void initialize(Max constraintAnnotation) {
        this.maxValue = constraintAnnotation.value();
        this.onlyfor = constraintAnnotation.onlyfor();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cxt) {
        if (Arrays.asList(this.onlyfor).contains("numeric") && Helper.isNumeric(value)) {
            return Long.parseLong((String) value) <= this.maxValue;
        }
        if (value instanceof String) {
            return ((String) value).length() <= this.maxValue;
        }
        if (value instanceof List) {
            return ((List<?>) value).size() <= this.maxValue;
        }
        return value == null;
    }
}
