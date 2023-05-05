package uz.test.abitur.dtos.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectShowDTO {
    @JsonProperty("id")
    private Integer id;

    private String name;

    @JsonProperty("is_mandatory")
    private boolean mandatory;

}
