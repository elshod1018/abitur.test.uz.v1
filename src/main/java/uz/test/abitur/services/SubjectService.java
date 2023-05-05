package uz.test.abitur.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.Subject;
import uz.test.abitur.dtos.subject.SubjectCreateDTO;
import uz.test.abitur.dtos.subject.SubjectResultDTO;
import uz.test.abitur.dtos.subject.SubjectShowDTO;
import uz.test.abitur.dtos.subject.SubjectUpdateDTO;
import uz.test.abitur.ex_handler.exceptions.NotFoundException;
import uz.test.abitur.ex_handler.exceptions.SubjectHasQuestionException;
import uz.test.abitur.repositories.SubjectRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserSession userSession;
    private final QuestionService questionService;

    public SubjectService(SubjectRepository subjectRepository,
                          UserSession userSession,
                          @Lazy QuestionService questionService) {
        this.subjectRepository = subjectRepository;
        this.userSession = userSession;
        this.questionService = questionService;
    }

    /*@CacheEvict(cacheNames = {"subjectCache"}, allEntries = true)*/
    public Subject create(SubjectCreateDTO dto) {
        return subjectRepository.save(
                Subject.builder()
                        .createdBy(userSession.getId())
                        .name(dto.getName())
                        .code(dto.getCode().toUpperCase())
                        .mandatory(dto.isMandatory())
                        .build());
    }

  /*  @Cacheable(value = "subjectResultDTOCache", key = "#id")*/
    public SubjectResultDTO getSubjectResultDTOById(Integer id) {
        SubjectResultDTO subjectById = subjectRepository.getSubjectResultDTOById(id);
        if (subjectById == null) {
            throw new NotFoundException("Subject not found");
        }
        return subjectById;
    }

   /* @Cacheable(value = "subjectShowDTOCache", key = "#id")*/
    public SubjectShowDTO getSubjectShowDTOById(Integer id) {
        return subjectRepository.getSubjectShowDTOById(id);
    }

    /*@CachePut(cacheNames = {"subjectCache", "subjectResultDTOCache", "subjectShowDTOCache"}, key = "#dto.id")*/
    public Subject update(SubjectUpdateDTO dto) {
        Optional<Subject> sub = subjectRepository.findById(dto.getId());
        Subject subject = null;
        if (sub.isPresent()) {
            subject = sub.get();
            subject.setUpdatedAt(LocalDateTime.now());
            subject.setUpdatedBy(userSession.getId());
            subject.setCode(dto.getCode().toUpperCase());
            subject.setName(dto.getName());
            subject.setMandatory(dto.isMandatory());
            subjectRepository.save(subject);
        }
        return subject;
    }

   /* @CacheEvict(value = "subjectCache", key = "#id")*/
    public void delete(Integer id) {
        Subject subject = findById(id);
        if (questionService.findBySubjectId(id).isPresent()) {
            throw new SubjectHasQuestionException("Subject with name %s has questions".formatted(subject.getName()));
        }
        subject.setDeleted(true);
        subjectRepository.save(subject);
    }

    /*@Cacheable(value = "subjectCache", key = "#subjectId")*/
    public Subject findById(Integer subjectId) {
        return subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Question Not found"));
    }


    /*@Cacheable(value = "subjectCache", key = "#root.method.name")*/
    public List<SubjectResultDTO> getAll() {
        return subjectRepository.getAll();
    }

   /* @Cacheable(value = "subjectCache", key = "#root.method.name")*/
    public List<Subject> getMandatory() {
        return subjectRepository.findMandatory();
    }
}
