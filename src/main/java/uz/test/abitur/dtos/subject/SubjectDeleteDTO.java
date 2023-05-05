package uz.test.abitur.dtos.subject;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectDeleteDTO {

    private Integer id;

    private String name;

}
