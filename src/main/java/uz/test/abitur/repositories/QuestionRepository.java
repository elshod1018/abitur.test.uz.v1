package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, String> {

    @Query(
            nativeQuery = true,
            value = """
                    select
                    q.id,
                    s.id, s.created_by, s.name, s.code, s.is_mandatory, s.created_at,
                    q.text, text(aqia.answers)
                    from Question q
                             join Subject s on s.id = q.subject_id and s.is_deleted = false
                             join (select answer.question_id as a_q_id, jsonb_agg(answer) as answers
                                   from Answer answer
                                   group by answer.question_id) as aqia on aqia.a_q_id = q.id
                    where q.is_deleted = false
                    order by q.created_at desc;
                    """)
    List<Object[]> getAll();

    @Query(
            nativeQuery = true,
            value = """
                    select
                    q.id,
                    s.id, s.created_by, s.name, s.code, s.is_mandatory, s.created_at,
                    q.text, text(aqia.answers)
                    from Question q
                             join Subject s on s.id = q.subject_id and s.is_deleted = false
                             join (select answer.question_id as a_q_id, jsonb_agg(answer) as answers
                                   from Answer answer
                                   group by answer.question_id) as aqia on aqia.a_q_id = q.id
                    where q.is_deleted = false and q.subject_id = ?1
                    order by q.created_at desc;
                    """)
    List<Object[]> getAllBySubjectId(Integer subjectId);

    @Query(
            nativeQuery = true,
            value = """
                    select
                    q.id,
                    s.id, s.created_by, s.name, s.code, s.is_mandatory, s.created_at,
                    q.text, text(aqia.answers)
                    from Question q
                             join Subject s on s.id = q.subject_id and s.is_deleted = false
                             join (select answer.question_id as a_q_id, jsonb_agg(answer) as answers
                                   from Answer answer
                                   group by answer.question_id) as aqia on aqia.a_q_id = q.id
                    where q.is_deleted = false and q.id = ?1
                    """)
    List<Object[]> getQuestionById(String id);

    @Query("select q from Question q where q.deleted = false and q.subjectId = ?1 order by RANDOM() limit 1")
    Optional<Question> findBySubjectId(Integer id);

    @Query(value = """
            select * from Question q where q.is_deleted = false and q.subject_id = ?1 ORDER BY random() limit ?2""",
            nativeQuery = true)
    List<Question> findNRandom(Integer subjectId, Integer count);


}