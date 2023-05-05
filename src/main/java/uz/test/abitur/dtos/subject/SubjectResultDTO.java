package uz.test.abitur.dtos.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SubjectResultDTO {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("created_by")
    private String createdBy;

    private String name;

    private String code;

    @JsonProperty("is_mandatory")
    private boolean mandatory;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
