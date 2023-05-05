package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.Document;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, String> {
    @Query("select d from Document d where d.generatedName = ?1")
    Optional<Document> findByGeneratedName(String generatedName);
}