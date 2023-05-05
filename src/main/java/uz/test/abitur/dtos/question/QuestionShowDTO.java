package uz.test.abitur.dtos.question;

import lombok.*;
import uz.test.abitur.domains.Answer;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class QuestionShowDTO {
    private String id;

    private Integer testSessionId;

    private Integer subjectId;

    private String text;

    private List<Answer> answers;

    private String userAnswerId;

    public QuestionShowDTO(String id, Integer testSessionId, Integer subjectId, String text, List<Answer> answers, String userAnswerId) {
        this.id = id;
        this.testSessionId = testSessionId;
        this.subjectId = subjectId;
        this.text = text;
        this.answers = answers;
        this.userAnswerId = userAnswerId;
    }
}
