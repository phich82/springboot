package com.example.demo.validations.rules;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * ElementType.TYPE: only apply for class level
 */
@Constraint(validatedBy = ExistByValidator.class)
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
public @interface ExistBy {
    public String message() default "Field value doesn't exist!";

    /**
     * Form fields mapped with table columns in database
     * Format: form_field1:table_column1,form_field2:table_column2,...
     * If only `form_field1, form_field2`, column1=form_field1, column2=form_field2
     */
    String fields();
    /**
     * Table name
     */
    String table();

    //represents group of constraints
    public Class<?>[] groups() default {};
    //represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @interface List {
        ExistBy[] value();
    }
}
