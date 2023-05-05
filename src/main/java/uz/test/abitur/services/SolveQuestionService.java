package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.test.abitur.domains.Answer;
import uz.test.abitur.domains.Question;
import uz.test.abitur.domains.SolveQuestion;
import uz.test.abitur.dtos.question.QuestionResultDTO;
import uz.test.abitur.dtos.question.QuestionShowDTO;
import uz.test.abitur.dtos.subject.SubjectShowDTO;
import uz.test.abitur.dtos.test.TestSessionResultDTO;
import uz.test.abitur.repositories.SolveQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SolveQuestionService {
    private final SolveQuestionRepository solveQuestionRepository;
    private final QuestionService questionService;

    public void create(TestSessionResultDTO dto) {
        SubjectShowDTO firstSubject = dto.getFirstSubject();
        SubjectShowDTO secondSubject = dto.getSecondSubject();
        SubjectShowDTO thirdSubject = dto.getThirdSubject();
        SubjectShowDTO fourthSubject = dto.getFourthSubject();
        SubjectShowDTO fifthSubject = dto.getFifthSubject();
        if (firstSubject != null) {
            save(firstSubject.getId(), dto.getId(), 2);
        }
        if (secondSubject != null) {
            save(secondSubject.getId(), dto.getId(), 2);
        }
        if (thirdSubject != null) {
            save(thirdSubject.getId(), dto.getId(), 1);
        }
        if (fourthSubject != null) {
            save(fourthSubject.getId(), dto.getId(), 1);
        }
        if (fifthSubject != null) {
            save(fifthSubject.getId(), dto.getId(), 1);
        }
    }

    private void save(Integer subjectId, Integer sessionId, Integer count) {
        List<Question> questionList = questionService.findNRand(subjectId, count);
        List<SolveQuestion> solveQuestions = new ArrayList<>();
        questionList.forEach(question -> solveQuestions.add(SolveQuestion.builder()
                .testSessionId(sessionId)
                .subjectId(subjectId)
                .questionId(question.getId())
                .build())
        );
        solveQuestionRepository.saveAll(solveQuestions);
    }

    public List<SolveQuestion> find(Integer testSessionId, Integer subjectId) {
        return solveQuestionRepository.findByTestSessionIdAndSubjectId(testSessionId, subjectId);
    }


    public QuestionShowDTO findQuestion(Integer testSessionId, Integer subjectId, String questionId) {
        SolveQuestion solveQuestion = getSolveQuestion(testSessionId, subjectId, questionId);
        QuestionResultDTO questionResultDTO = questionService.getById(questionId);
        List<Answer> answers = questionResultDTO.getAnswers();
        answers.forEach(answer -> answer.setIsTrue(null));
        return new QuestionShowDTO(questionId, testSessionId, subjectId, questionResultDTO.getText(), answers, solveQuestion.getUserAnswerId());
    }

    public boolean update(Integer testSessionId, Integer subjectId, String questionId, String answerId) {
        SolveQuestion solveQuestion = getSolveQuestion(testSessionId, subjectId, questionId);
        solveQuestion.setUserAnswerId(answerId);
        solveQuestionRepository.save(solveQuestion);
        return true;
    }

    public int getCount(Integer testSessionId, Integer subjectId) {
        return solveQuestionRepository.getCount(testSessionId, subjectId);
    }

    private SolveQuestion getSolveQuestion(Integer testSessionId, Integer subjectId, String questionId) {
        SolveQuestion solveQuestion = solveQuestionRepository
                .findByTestSessionIdAndSubjectIdAndQuestionId(testSessionId, subjectId, questionId);
        if (Objects.isNull(solveQuestion)) {
            throw new RuntimeException("Solve question not found");
        }
        return solveQuestion;
    }
    public List<Boolean> getListOfAnswerTrueOrFalse(Integer testSessionId, Integer subjectId){
        return solveQuestionRepository.getListOfAnswerTrueOrFalse(testSessionId, subjectId);
    }
}
