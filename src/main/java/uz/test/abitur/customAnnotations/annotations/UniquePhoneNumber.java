package uz.test.abitur.customAnnotations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import uz.test.abitur.customAnnotations.validators.UniqueNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNumberValidator.class)
public @interface UniquePhoneNumber {
    String message() default "Phone Number is already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
