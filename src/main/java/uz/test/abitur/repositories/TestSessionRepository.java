package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.TestSession;

import java.util.List;
import java.util.Optional;

public interface TestSessionRepository extends JpaRepository<TestSession, Integer> {
    @Query("select t from TestSession t where t.finished = false and t.finishedAt < now()")
    List<TestSession> findUnfinishedTestSession();
    @Query(value = """
            select t.* from test_session t
            where t.user_id = ?1 and
            t.is_finished = false and
            t.finished_at >( CURRENT_TIMESTAMP + interval '1' MINUTE)""",
            nativeQuery = true)
    Optional<TestSession> findActive(String userId);


    @Query("select t from TestSession t where t.finished = false and t.userId = ?1 and t.id = ?2")
    Optional<TestSession> findById(String userId, Integer integer);
}