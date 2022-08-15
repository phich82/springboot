package com.example.demo.validations.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repositories.DB;


public class ExistByValidator implements ConstraintValidator<ExistBy, Object> {

    private String fields;
    private String table;
    private String message;

    @Autowired
    DB db;

    /**
     * Access properties of target annotation
     */
    @Override
    public void initialize(ExistBy annotation) {
        this.fields = annotation.fields();
        this.table = annotation.table();
        this.message = annotation.message();
    }

    /**
     * Applied for class, instanceTarget is an instance of this applied class
     */
    @Override
    public boolean isValid(Object instanceTarget, ConstraintValidatorContext context) {

        String[] fields = this.fields.split(",");

        Map<String, Object> params = new HashMap<String, Object>();

        BeanWrapperImpl instance = new BeanWrapperImpl(instanceTarget);

        // Check fields
        for (String field: fields) {
            if (field == null || field.equals("")) {
                continue;
            }
            String[] parts = field.split(":");
            String fieldName = parts[0];
            String columnName = parts.length > 1 ? parts[1] : fieldName;
            Object fieldValue = null;
            // Check exists properties of class
            try {
                fieldValue = instance.getPropertyValue(fieldName);
                params.put(columnName, fieldValue);
            } catch(Exception e) {
                String errorMessage = String.format("Property `%s` not exist in `%s` class.", fieldName, instanceTarget.getClass().getSimpleName());
                throw new ConstraintDeclarationException(errorMessage);
            }
            // Check exists field of parameters
            if (fieldValue == null) {
                throw new ConstraintDeclarationException(String.format("Field `%s` is missing.", fieldName));
            }
        }

        // Fields not exists
        if (params.size() < 1) {
            throw new ConstraintDeclarationException("Attribute `fields` must not be empty in `ExistBy` rule.");
        }

        boolean valid = db.table(this.table).withHolderColumn().existsBy(params);

        // Specify the field for error message
        if (!valid) {
            context.buildConstraintViolationWithTemplate(this.message)
                .addPropertyNode(fields[0])
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        }

        return valid;
    }
}
