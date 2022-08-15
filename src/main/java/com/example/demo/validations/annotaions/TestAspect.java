package com.example.demo.validations.annotaions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Apply (aspect) for method level
 */
@Target({METHOD})
@Retention(RUNTIME)
@Documented
public @interface TestAspect {
    String level() default "simple";
}
