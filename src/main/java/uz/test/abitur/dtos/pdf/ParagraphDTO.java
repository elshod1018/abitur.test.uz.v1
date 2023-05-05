package uz.test.abitur.dtos.pdf;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ParagraphDTO {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @Builder.Default
    private List<Double> scores = new ArrayList<>();
}
