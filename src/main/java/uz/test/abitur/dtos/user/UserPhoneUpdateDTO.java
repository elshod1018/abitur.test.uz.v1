package uz.test.abitur.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneUpdateDTO {
    private String oldPassword;

    private String newPassword;

    private String newConfirmPassword;

}
