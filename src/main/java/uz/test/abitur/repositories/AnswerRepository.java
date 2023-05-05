package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, String> {
    @Query("select a from Answer a where a.questionId = ?1")
    List<Answer> findByQuestionId(String questionId);

}