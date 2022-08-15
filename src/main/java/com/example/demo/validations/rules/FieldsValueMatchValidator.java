package com.example.demo.validations.rules;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;


public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {

    private String field;
    private String fieldMatch;
    private String message;

    /**
     * Access properties of target annotation
     */
    @Override
    public void initialize(FieldsValueMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    /**
     * Applied for class, instanceTarget is an instance of this applied class
     */
    @Override
    public boolean isValid(Object instanceTarget, ConstraintValidatorContext context) {
        Object fieldValue = null;
        Object fieldMatchValue  = null;
        BeanWrapperImpl instance = new BeanWrapperImpl(instanceTarget);

        // Check exists property in target class
        try {
            fieldValue = instance.getPropertyValue(field);
        } catch(Exception e) {
            throw new ConstraintDeclarationException(String.format("Property `%s` not exists in %s class.", field, instanceTarget.getClass().getSimpleName()));
        }
        // Check exists property in target class
        try {
            fieldMatchValue = instance.getPropertyValue(fieldMatch);
        } catch(Exception e) {
            throw new ConstraintDeclarationException(String.format("Property `%s` not exists in %s class.", fieldMatch, instanceTarget.getClass().getSimpleName()));
        }
        // Check exists fields in parameters
        if (fieldValue == null) {
            throw new ConstraintDeclarationException(String.format("Field `%s` is missing.", field));
        }
        if (fieldMatchValue == null) {
            throw new ConstraintDeclarationException(String.format("Field `%s` is missing.", fieldMatch));
        }

        boolean valid = fieldValue != null
            ? fieldValue.equals(fieldMatchValue)
            : fieldMatchValue == null;

        // Specify the field for error message
        if (!valid) {
            context.buildConstraintViolationWithTemplate(this.message)
                .addPropertyNode(this.field)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        }

        return valid;
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
