package uz.test.abitur.dtos.pdf;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestHistoryPDFGenerateDTO {
    private String subjectName;
    private List<Boolean> listOfTrueOrFalseAnswers;
}
