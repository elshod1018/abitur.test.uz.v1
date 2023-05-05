package uz.test.abitur.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test_session")
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "first_subject_id")
    private Integer firstSubjectId;

    @Column(name = "second_subject_id")
    private Integer secondSubjectId;

    @Column(name = "third_subject_id")
    private Integer thirdSubjectId;

    @Column(name = "fourth_subject_id")
    private Integer fourthSubjectId;

    @Column(name = "fifth_subject_id")
    private Integer fifthSubjectId;

    @Column(name = "is_finished")
    private boolean finished;
}
