package uz.test.abitur.dtos.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserSMSCreateDTO {
    private String code;
    private String userId;
    private String phoneNumber;
}
