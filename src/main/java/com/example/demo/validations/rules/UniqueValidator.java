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


public class UniqueValidator implements ConstraintValidator<Unique, Object> {

    private String fields;
    private String table;
    private String pk;
    private String pk_type;
    private String message;

    @Autowired
    DB db;

    /**
     * Access properties of target annotation
     */
    @Override
    public void initialize(Unique annotation) {
        this.fields = annotation.fields();
        this.table = annotation.table();
        this.pk = annotation.pk();
        this.pk_type = annotation.pk_type();
        this.message = annotation.message();
    }

    /**
     * Applied for class, instanceTarget is an instance of this applied class
     */
    @Override
    public boolean isValid(Object instanceTarget, ConstraintValidatorContext context) {

        String[] fields = this.fields.split(",");
        String[] pKeys = this.pk.split(",");

        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> paramsPK = new HashMap<String, Object>();

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

        // Check primary keys if found
        for (String k: pKeys) {
            if (k == null || k.equals("")) {
                continue;
            }
            String[] parts = k.split(":");
            String fieldName = parts[0];
            String columnName = parts.length > 1 ? parts[1] : fieldName;
            Object fieldValue = null;
            // Check exists properties of class
            try {
                fieldValue = instance.getPropertyValue(fieldName);
                paramsPK.put(columnName, new Object[] {"<>", fieldValue});
            } catch(Exception e) {
                String errorMessage = String.format("Property `%s` not exist in `%s` class.", fieldName, instanceTarget.getClass().getSimpleName());
                throw new ConstraintDeclarationException(errorMessage);
            }
            // Check exists field of parameters
            if (fieldValue == null) {
                throw new ConstraintDeclarationException(String.format("Field `%s` is missing.", fieldName));
            }
        }

        // Merge params of primary key into it
        params.putAll(paramsPK);

        boolean valid = !db.table(this.table).withHolderColumn().existsBy(params);

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