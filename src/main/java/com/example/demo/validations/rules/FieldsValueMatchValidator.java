package com.example.demo.validations.rules;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;

    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

        if (fieldValue != null) {
            return fieldValue.equals(fieldMatchValue);
        }
        return fieldMatchValue == null;
    }
}

// @FieldsValueMatch.List({
//     @FieldsValueMatch(
//       field = "password",
//       fieldMatch = "verifyPassword",
//       message = "Passwords do not match!"
//     ),
//     @FieldsValueMatch(
//       field = "email",
//       fieldMatch = "verifyEmail",
//       message = "Email addresses do not match!"
//     )
// })
// public class NewUserForm {
//     private String email;
//     private String verifyEmail;
//     private String password;
//     private String verifyPassword;

//     // standard constructor, getters, setters
// }
