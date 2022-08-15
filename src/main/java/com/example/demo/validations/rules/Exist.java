package com.example.demo.validations.rules;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Apply for values (METHOD, FIELD, ANNOTATION_TYPE, PARAMETER)
 */
@Constraint(validatedBy = ExistValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface Exist {
    public String message() default "Field value doesn't exist!";

    /** Table column name in database */
    String field() default "id";
    /** Table name */
    String table();

    //represents group of constraints
    public Class<?>[] groups() default {};
    //represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, PARAMETER})
    @Retention(RUNTIME)
    @interface List {
        Exist[] value();
    }
}
