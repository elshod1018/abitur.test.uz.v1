package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.SolveQuestion;

import java.util.List;

public interface SolveQuestionRepository extends JpaRepository<SolveQuestion, Integer> {
    @Query("select s from SolveQuestion s where s.testSessionId = ?1 and s.subjectId = ?2")
    List<SolveQuestion> findByTestSessionIdAndSubjectId(Integer testSessionId, Integer subjectId);

    @Query("select s from SolveQuestion s where s.testSessionId = ?1 and s.subjectId = ?2 and s.questionId = ?3")
    SolveQuestion findByTestSessionIdAndSubjectIdAndQuestionId(Integer testSessionId, Integer subjectId, String questionId);

    @Query("""
             select count(s) from SolveQuestion s
             join Answer as a on a.id = s.userAnswerId
             where s.testSessionId = ?1 and s.subjectId = ?2 and a.isTrue = true""")
    int getCount(Integer testSessionId, Integer subjectId);


    @Query(nativeQuery = true, value = """
            select a.id like s.user_answer_id from solve_question s
                         join answer as a on a.question_id = s.question_id and a.is_true = true
            where s.test_session_id = ?1 and s.subject_id =?2""")
    List<Boolean> getListOfAnswerTrueOrFalse(Integer testSessionId, Integer subjectId);
}