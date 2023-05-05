package uz.test.abitur.controller.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.SolveQuestion;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.dtos.question.QuestionShowDTO;
import uz.test.abitur.dtos.test.TestSessionCreateDTO;
import uz.test.abitur.dtos.test.TestSessionResultDTO;
import uz.test.abitur.events_listeners.events.TestSessionStartedEvent;
import uz.test.abitur.services.SolveQuestionService;
import uz.test.abitur.services.SubjectService;
import uz.test.abitur.services.TestHistoryService;
import uz.test.abitur.services.TestSessionService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TestController {
    private final SubjectService subjectService;
    private final TestSessionService testSessionService;
    private final SolveQuestionService solveQuestionService;
    private final ObjectMapper objectMapper;
    private final TestHistoryService testHistoryService;
    private final ApplicationEventPublisher publisher;
    private final UserSession userSession;

    @GetMapping
    public String testsPage(Model model) {
        TestSession testSession = testSessionService.findActiveTestSession();
        if (Objects.isNull(testSession)) {
            model.addAttribute("subjects", subjectService.getAll());
            return "test/start";
        }
        return "redirect:/test/history";
    }

    @PostMapping("/start")
    public String startTest(Model model, @Valid @ModelAttribute TestSessionCreateDTO dto) {
        if ((dto.getFirstSubjectId() == null || dto.getFirstSubjectId() == -1)
                && (dto.getSecondSubjectId() == null || dto.getSecondSubjectId() == -1)
                && (!dto.isMandatory())) {
            model.addAttribute("error", "You should choose at least one subject");
            model.addAttribute("subjects", subjectService.getAll());
            return "test/start";
        }
        TestSession testSession = testSessionService.findActiveTestSession();
        if (!Objects.isNull(testSession)) {
            return "redirect:/test/continue/" + testSession.getId();
        }
        TestSessionResultDTO sessionResultDTO = testSessionService.create(dto);
        model.addAttribute("testSession", sessionResultDTO);
        publisher.publishEvent(new TestSessionStartedEvent(sessionResultDTO));
        return "test/tests";
    }

    @GetMapping("/get/{testSessionId:.*}/{subjectId:.*}")
    @ResponseBody
    public String getSubjects(@PathVariable(value = "testSessionId") Integer testSessionId, @PathVariable("subjectId") Integer subjectId) throws JsonProcessingException {
        List<SolveQuestion> solveQuestions = solveQuestionService.find(testSessionId, subjectId);
        return objectMapper.writeValueAsString(solveQuestions);
    }

    @GetMapping("/get/{testSessionId:.*}/{subjectId:.*}/{questionId:.*}")
    @ResponseBody
    public String getQuestion(@PathVariable(value = "testSessionId") Integer testSessionId,
                              @PathVariable("subjectId") Integer subjectId,
                              @PathVariable("questionId") String questionId) throws JsonProcessingException {
        QuestionShowDTO question = solveQuestionService.findQuestion(testSessionId, subjectId, questionId);
        return objectMapper.writeValueAsString(question);
    }

    @GetMapping("/update/{testSessionId:.*}/{subjectId:.*}/{questionId:.*}/{answerId:.*}")
    @ResponseBody
    public String updateSolveQuestion(@PathVariable(value = "testSessionId") Integer testSessionId,
                                      @PathVariable("subjectId") Integer subjectId,
                                      @PathVariable("questionId") String questionId,
                                      @PathVariable("answerId") String answerId) throws JsonProcessingException {
        solveQuestionService.update(testSessionId, subjectId, questionId, answerId);
        return "Updated";
    }

    @PostMapping("/finish/{testSessionId:.*}")
    public String finishTest(@PathVariable("testSessionId") Integer testSessionId) throws IOException {
        testSessionService.finish(testSessionId);
        return "redirect:/test/history";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER','SUPER_ADMIN')")
    @GetMapping("/history")
    public String testHistoryPage(Model model) {
        String role = userSession.getUser().getRole().name();
        if (role.equals("ADMIN") || role.equals("SUPER_ADMIN")) {
            model.addAttribute("histories", testHistoryService.getAllHistory());
        } else {
            model.addAttribute("histories", testHistoryService.userAllHistory(userSession.getId()));
        }
        return "test/history";
    }

    @GetMapping("/continue/{testSessionId:.*}")
    public String testContinue(Model model, @PathVariable Integer testSessionId) {
        TestSessionResultDTO sessionResultDTO = testSessionService.findById(testSessionId);
        model.addAttribute("testSession", sessionResultDTO);
        return "test/tests";
    }
}
