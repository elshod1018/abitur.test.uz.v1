package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import uz.test.abitur.domains.News;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query("select n from News n where n.deleted = false")
    List<News> getAll();
    @Override
    @NonNull Optional<News> findById(@NonNull Integer id);
}