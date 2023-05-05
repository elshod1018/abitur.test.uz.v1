package uz.test.abitur.customAnnotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import uz.test.abitur.customAnnotations.annotations.UniquePhoneNumber;
import uz.test.abitur.services.AuthUserService;

import java.util.Objects;

@RequiredArgsConstructor
public class UniqueNumberValidator  implements ConstraintValidator<UniquePhoneNumber, String> {

        private final AuthUserService authUserService;


    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return Objects.isNull(authUserService.getByPhoneAndDeletedFalse(phoneNumber));
    }
}
