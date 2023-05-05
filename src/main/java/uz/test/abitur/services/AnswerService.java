package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.test.abitur.domains.Answer;
import uz.test.abitur.dtos.question.QuestionCreateDTO;
import uz.test.abitur.dtos.question.QuestionUpdateDTO;
import uz.test.abitur.repositories.AnswerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public List<Answer> saveAll(QuestionCreateDTO dto, String questionId) {
        Answer answer1 = Answer.builder()
                .text(dto.getFirstAnswer())
                .questionId(questionId)
                .build();
        Answer answer2 = Answer.builder()
                .text(dto.getSecondAnswer())
                .questionId(questionId)
                .build();
        Answer answer3 = Answer.builder()
                .text(dto.getThirdAnswer())
                .questionId(questionId)
                .build();
        Answer answer4 = Answer.builder()
                .text(dto.getFourthAnswer())
                .questionId(questionId)
                .build();
        switch (dto.getRightAnswer()) {
            case "1" -> answer1.setIsTrue(true);
            case "2" -> answer2.setIsTrue(true);
            case "3" -> answer3.setIsTrue(true);
            case "4" -> answer4.setIsTrue(true);
        }
        return answerRepository.saveAll(List.of(answer1, answer2, answer3, answer4));

    }

    public List<Answer> updateAll(QuestionUpdateDTO dto, String id) {
        List<Answer> answers = answerRepository.findByQuestionId(id);
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            switch (i) {
                case 0 -> answer.setText(dto.getFirstAnswer());
                case 1 -> answer.setText(dto.getSecondAnswer());
                case 2 -> answer.setText(dto.getThirdAnswer());
                case 3 -> answer.setText(dto.getFourthAnswer());
            }
        }
        switch (dto.getRightAnswer()) {
            case "1" -> {
                answers.get(1).setIsTrue(false);
                answers.get(2).setIsTrue(false);
                answers.get(3).setIsTrue(false);
                answers.get(0).setIsTrue(true);
            }
            case "2" -> {
                answers.get(0).setIsTrue(false);
                answers.get(2).setIsTrue(false);
                answers.get(3).setIsTrue(false);
                answers.get(1).setIsTrue(true);
            }
            case "3" -> {
                answers.get(0).setIsTrue(false);
                answers.get(1).setIsTrue(false);
                answers.get(3).setIsTrue(false);
                answers.get(2).setIsTrue(true);
            }
            case "4" -> {
                answers.get(0).setIsTrue(false);
                answers.get(1).setIsTrue(false);
                answers.get(2).setIsTrue(false);
                answers.get(3).setIsTrue(true);
            }
        }
        return answerRepository.saveAll(answers);
    }
}
