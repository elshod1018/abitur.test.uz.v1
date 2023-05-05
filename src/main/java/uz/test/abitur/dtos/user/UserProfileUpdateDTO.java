package uz.test.abitur.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfileUpdateDTO {

    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    private String lastName;

    private MultipartFile profilePicture;
}
