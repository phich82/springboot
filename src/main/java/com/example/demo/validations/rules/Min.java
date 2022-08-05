package com.example.demo.validations.rules;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy=MinValidator.class)
public @interface Min {
    //error message
    public String message() default "The field must be higher than the minimum value.";

    /**
	 * @return value the element must be higher or equal to
	 */
	long value();

	/**
	 * Can be string or number or list
	 *
	 * Specify the data type for field. Default: string
	 */
	String[] onlyfor() default {"string", "numeric", "list"};

    //represents group of constraints
    public Class<?>[] groups() default {};
    //represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};

    /**
	 * Defines several {@link Min} annotations on the same element.
	 *
	 * @see Min
	 */
	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {

		Min[] value();
	}
}
