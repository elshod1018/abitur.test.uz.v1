package uz.test.abitur.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.test.abitur.domains.Subject;
import uz.test.abitur.dtos.subject.SubjectResultDTO;
import uz.test.abitur.dtos.subject.SubjectShowDTO;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    @Query("""
            select new uz.test.abitur.dtos.subject.SubjectResultDTO(s.id, a.firstName , s.name, s.code, s.mandatory, s.createdAt)
            from Subject s
            INNER JOIN AuthUser a on a.id = s.createdBy
            where s.deleted = false order by s.name""")
    List<SubjectResultDTO> getAll();

    @Query("select s from Subject s where s.mandatory = true and s.deleted = false order by s.name")
    List<Subject> findMandatory();

    @Query("""
            select new uz.test.abitur.dtos.subject.SubjectResultDTO(s.id,a.firstName , s.name, s.code, s.mandatory, s.createdAt)
            from Subject s
            INNER JOIN AuthUser a on a.id = s.createdBy
            where s.deleted = false and s.id = ?1""")
    SubjectResultDTO getSubjectResultDTOById(Integer id);

    @Query("""
            select new uz.test.abitur.dtos.subject.SubjectShowDTO(s.id,s.name,s.mandatory)
            from Subject s where s.deleted = false and s.id = ?1""")
    SubjectShowDTO getSubjectShowDTOById(Integer id);

    @Query("select s from Subject s where s.name = ?1 and s.mandatory = ?2 and s.deleted = false")
    Subject findByNameAndMandatory(String subjectName, boolean isMandatory);
}