package uz.test.abitur.dtos.question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uz.test.abitur.domains.Answer;
import uz.test.abitur.dtos.subject.SubjectResultDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@ToString
public class QuestionResultDTO {
    private String id;

    private SubjectResultDTO subject;

    private String text;

    private List<Answer> answers;

    public QuestionResultDTO(String questionId,
                             Integer subjectId, String subjectCreatedBy, String subjectName, String subjectCode, boolean mandatory, LocalDateTime subjectCreatedAt,
                             String text, List<Answer> answers) {
        this.id = questionId;
        this.subject = new SubjectResultDTO(subjectId, subjectCreatedBy, subjectName, subjectCode, mandatory, subjectCreatedAt);
        this.text = text;
        this.answers = answers;
    }
}
