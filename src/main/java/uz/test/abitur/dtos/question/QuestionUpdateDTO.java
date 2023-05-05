package uz.test.abitur.dtos.question;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateDTO {
    @NotBlank
    private String id;

    private Integer subjectId;

    @NotBlank
    private String text;

    private String rightAnswer;

    @NotBlank
    private String firstAnswer;

    @NotBlank
    private String secondAnswer;

    @NotBlank
    private String thirdAnswer;
    @NotBlank
    private String fourthAnswer;

}
