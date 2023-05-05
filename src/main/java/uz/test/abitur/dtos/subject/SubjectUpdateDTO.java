package uz.test.abitur.dtos.subject;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectUpdateDTO {
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private boolean mandatory;
}
