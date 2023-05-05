package uz.test.abitur.controller.question;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.domains.Question;
import uz.test.abitur.dtos.question.QuestionCreateDTO;
import uz.test.abitur.dtos.question.QuestionDeleteDTO;
import uz.test.abitur.dtos.question.QuestionResultDTO;
import uz.test.abitur.dtos.question.QuestionUpdateDTO;
import uz.test.abitur.dtos.subject.SubjectResultDTO;
import uz.test.abitur.services.AnswerService;
import uz.test.abitur.services.QuestionService;
import uz.test.abitur.services.SubjectService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class QuestionController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final SubjectService subjectService;
    private final ObjectMapper objectMapper;

    @GetMapping("/all")
    public String questionsPage(Model model, @RequestParam(required = false) Integer subjectId) {
        List<SubjectResultDTO> allSubject = subjectService.getAll();
        List<QuestionResultDTO> questions;
        if (Objects.isNull(subjectId)) {
            questions = questionService.getAll();
        } else {
            questions = questionService.getAllBySubjectId(subjectId);
        }
        model.addAttribute("subjects", allSubject);
        model.addAttribute("questions", questions);
        return "question/questions";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute QuestionCreateDTO dto) {
        Question question = questionService.create(dto);
        answerService.saveAll(dto, question.getId());
        return "redirect:/questions/all";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute QuestionUpdateDTO dto) {
        Question question = questionService.update(dto);
        answerService.updateAll(dto, question.getId());
        return "redirect:/questions/all";
    }

    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute QuestionDeleteDTO dto) {
        questionService.delete(dto);
        return "redirect:/questions/all";
    }


    @GetMapping("/get/{id:.*}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public String get(@PathVariable String id) throws JsonProcessingException {
        QuestionResultDTO question = questionService.getById(id);
        return objectMapper.writeValueAsString(question);
    }
}
