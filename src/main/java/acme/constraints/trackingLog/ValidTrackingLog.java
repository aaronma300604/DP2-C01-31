
package acme.constraints.trackingLog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrackingLogValidator.class)
public @interface ValidTrackingLog {

	String message() default "Resolution percentage must be higher than the highest resolution for this claim.";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
