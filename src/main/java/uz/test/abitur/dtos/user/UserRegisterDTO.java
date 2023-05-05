package uz.test.abitur.dtos.user;

import jakarta.validation.constraints.*;
import lombok.*;
import uz.test.abitur.customAnnotations.annotations.UniquePhoneNumber;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @UniquePhoneNumber
    @Pattern(regexp = "(\\+998)[0-9]{2}[0-9]{3}[0-9]{2}[0-9]{2}")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8, max = 16, message = "Password must be between {min} and {max}")
    private String password;

    @Size(min = 8, max = 16, message = "Confirm password must be between {min} and {max}")
    private String confirmPassword;
}
