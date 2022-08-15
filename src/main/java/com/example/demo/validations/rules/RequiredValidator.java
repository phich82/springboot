package com.example.demo.validations.rules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class RequiredValidator implements ConstraintValidator<Required, Object>
{

    public void initialize(Required constraintAnnotation) {}

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cxt) {
        return value != null;
    }
}
