package com.example.demo.validations.rules;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repositories.DB;

public class ExistValidator implements ConstraintValidator<Exist, Object> {

    private String field;
    private String table;

    @Autowired
    DB db;

    /**
     * Access properties of target annotation
     */
    @Override
    public void initialize(Exist annotation) {
        this.field = annotation.field();
        this.table = annotation.table();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put(this.field, value);

        return db.table(this.table).existsBy(params);
    }
}
