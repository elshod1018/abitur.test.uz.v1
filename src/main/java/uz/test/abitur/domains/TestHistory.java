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
@ToString
@Table(name = "test_history")
public class TestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @Column(nullable = false, name = "test_session_id")
    private Integer testSessionId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "first_subject")
    private String firstSubject;

    @Column(name = "second_subject")
    private String secondSubject;

    @Column(name = "third_subject")
    private String thirdSubject;

    @Column(name = "fourth_subject")
    private String fourthSubject;

    @Column(name = "fifth_subject")
    private String fifthSubject;

    @Column(name = "total_score")
    private Double totalScore;

    @Column(name = "document_id")
    private String documentId;
}
