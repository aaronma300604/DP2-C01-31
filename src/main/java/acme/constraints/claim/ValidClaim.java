
package acme.constraints.claim;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClaimValidator.class)
public @interface ValidClaim {

	String message() default "Leg must have occurred";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
