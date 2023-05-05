package uz.test.abitur.dtos.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDTO {

    @NotNull
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
