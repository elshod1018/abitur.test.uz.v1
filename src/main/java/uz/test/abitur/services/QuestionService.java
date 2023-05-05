package uz.test.abitur.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.Answer;
import uz.test.abitur.domains.Question;
import uz.test.abitur.dtos.question.QuestionCreateDTO;
import uz.test.abitur.dtos.question.QuestionDeleteDTO;
import uz.test.abitur.dtos.question.QuestionResultDTO;
import uz.test.abitur.dtos.question.QuestionUpdateDTO;
import uz.test.abitur.ex_handler.exceptions.NotFoundException;
import uz.test.abitur.repositories.QuestionRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserSession userSession;
    private final ObjectMapper objectMapper;
    private final SubjectService subjectService;

    public Question create(QuestionCreateDTO dto) {
        return questionRepository.save(Question.builder()
                .createdBy(userSession.getId())
                .text(dto.getText())
                .subjectId(dto.getSubjectId())
                .build());
    }

   /* @CachePut(value = "questionCache", key = "#dto.id")*/
    public Question update(QuestionUpdateDTO dto) {
        Question question = questionRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Integer subjectId = dto.getSubjectId();
        subjectService.findById(subjectId);

        question.setText(dto.getText());
        question.setSubjectId(subjectId);
        question.setUpdatedAt(LocalDateTime.now());
        question.setUpdatedBy(userSession.getId());

        return questionRepository.save(question);
    }

  /*  @CacheEvict(value = "questionCache", key = "#dto.id")*/
    public void delete(QuestionDeleteDTO dto) {
        Question question = questionRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Question not found"));
        question.setUpdatedAt(LocalDateTime.now());
        question.setUpdatedBy(userSession.getId());
        question.setDeleted(true);
        questionRepository.save(question);
    }

    /*@Cacheable(value = "questionCache", key = "#root.methodName")*/
    public List<QuestionResultDTO> getAll() {
        List<Object[]> objectList = questionRepository.getAll();
        if (objectList.isEmpty())
            return null;
        return objectList.stream()
                .map(this::mapObjectToQuestionResultDTO)
                .toList();
    }

    /*@Cacheable(value = "questionCache", key = "#subjectId")*/
    public List<QuestionResultDTO> getAllBySubjectId(Integer subjectId) {
        List<Object[]> objectList = questionRepository.getAllBySubjectId(subjectId);
        if (objectList.isEmpty())
            return null;
        return objectList.stream()
                .map(this::mapObjectToQuestionResultDTO)
                .toList();
    }

    /*@Cacheable(value = "questionCache", key = "#id")*/
    public QuestionResultDTO getById(String id) {
        List<Object[]> objectList = questionRepository.getQuestionById(id);
        if (objectList.isEmpty())
            throw new RuntimeException("Question not found");
        return this.mapObjectToQuestionResultDTO(objectList.get(0));
    }

    public Optional<Question> findBySubjectId(Integer id) {
        return questionRepository.findBySubjectId(id);
    }

    public List<Question> findNRand(Integer subjectId, Integer count) {
        return questionRepository.findNRandom(subjectId, count);
    }

    private QuestionResultDTO mapObjectToQuestionResultDTO(Object[] objects) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime createdAt = LocalDateTime.now();
        List<Answer> answers = new ArrayList<>();
        try {
            answers = objectMapper.readValue(objects[8].toString(), new TypeReference<>() {
            });
            String time = objects[6].toString().replace(" ", "T");
            createdAt = LocalDateTime.parse(time, dateTimeFormatter);
        } catch (Exception e) {
            System.out.println(" e = " + e.getMessage());
        }
        return new QuestionResultDTO((String) objects[0],
                (Integer) objects[1], (String) objects[2], (String) objects[3], (String) objects[4], (Boolean) objects[5], createdAt,
                (String) objects[7], answers);
    }

}
