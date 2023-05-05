package uz.test.abitur.dtos.test;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestSessionCreateDTO {

    private Integer firstSubjectId;

    private Integer secondSubjectId;

    private boolean mandatory;
}
