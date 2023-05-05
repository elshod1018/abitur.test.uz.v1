package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.TestHistory;

import java.util.List;

public interface TestHistoryRepository extends JpaRepository<TestHistory, Integer> {
    @Query("select t from TestHistory t where t.testSessionId = ?1")
    TestHistory findByTestSessionId(Integer testSessionId);
    @Query("select t from TestHistory t where t.userId = ?1 order by t.startedAt desc")
    List<TestHistory>findAll(String userId);
}