
package acme.constraints.phone;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})

@Pattern(regexp = "^\s*$|^\\+?\\d{6,15}$", message = "{acme.validation.phone}")
public @interface ValidPhone {

	String message() default "{acme.validation.phone.message}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
