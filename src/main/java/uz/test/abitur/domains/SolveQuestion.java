package uz.test.abitur.domains;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solve_question")
public class SolveQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "test_session_id")
    private Integer testSessionId;

    @Column(nullable = false, name = "subject_id")
    private Integer subjectId;

    @Column(nullable = false, name = "question_id")
    private String questionId;

    @Column(name = "user_answer_id")
    private String userAnswerId;
}
