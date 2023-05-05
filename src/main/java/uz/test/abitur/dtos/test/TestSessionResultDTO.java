package uz.test.abitur.dtos.test;

import lombok.*;
import uz.test.abitur.dtos.subject.SubjectShowDTO;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestSessionResultDTO {
    private Integer id;

    private SubjectShowDTO firstSubject;

    private SubjectShowDTO secondSubject;

    private SubjectShowDTO thirdSubject;

    private SubjectShowDTO fourthSubject;

    private SubjectShowDTO fifthSubject;
}
