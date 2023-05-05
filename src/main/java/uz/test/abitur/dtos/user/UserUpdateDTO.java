package uz.test.abitur.dtos.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import uz.test.abitur.enums.Status;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @NotBlank
    private String id;

    private Status status;
}
