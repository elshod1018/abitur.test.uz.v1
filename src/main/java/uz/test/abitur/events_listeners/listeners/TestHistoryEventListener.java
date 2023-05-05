package uz.test.abitur.events_listeners.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.AuthUser;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.dtos.pdf.ParagraphDTO;
import uz.test.abitur.dtos.pdf.TestHistoryPDFGenerateDTO;
import uz.test.abitur.events_listeners.events.FileCreatedEvent;
import uz.test.abitur.events_listeners.events.TestHistoryFinishedEvent;
import uz.test.abitur.services.AuthUserService;
import uz.test.abitur.services.SolveQuestionService;
import uz.test.abitur.services.SubjectService;
import uz.test.abitur.services.pdf.GeneratePDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
@RequiredArgsConstructor
public class TestHistoryEventListener {
    private final SolveQuestionService solveQuestionService;
    private final UserSession userSession;
    private final SubjectService subjectService;
    private final GeneratePDF generatePDF;
    private final AuthUserService authUserService;

    @Async
    @EventListener(value = TestHistoryFinishedEvent.class
            /*condition = "#event.testSession ne null",*/
    )
    public CompletableFuture<FileCreatedEvent> testHistoryFinishedEventListener(TestHistoryFinishedEvent event) throws IOException {
        TestSession testSession = event.getTestSession();
        Integer testSessionId = testSession.getId();
        Integer firstSubjectId = testSession.getFirstSubjectId();
        Integer secondSubjectId = testSession.getSecondSubjectId();
        Integer thirdSubjectId = testSession.getThirdSubjectId();
        Integer fourthSubjectId = testSession.getFourthSubjectId();
        Integer fifthSubjectId = testSession.getFifthSubjectId();
        int firstCount = solveQuestionService.getCount(testSessionId, firstSubjectId);
        int secondCount = solveQuestionService.getCount(testSessionId, secondSubjectId);
        int thirdCount = solveQuestionService.getCount(testSessionId, thirdSubjectId);
        int fourthCount = solveQuestionService.getCount(testSessionId, fourthSubjectId);
        int fifthCount = solveQuestionService.getCount(testSessionId, fifthSubjectId);

        List<TestHistoryPDFGenerateDTO> forPDF = new ArrayList<>();
        ParagraphDTO paragraphDTO = new ParagraphDTO();
        AuthUser user = authUserService.findById(testSession.getUserId());
        paragraphDTO.setFirstName(user.getFirstName());
        paragraphDTO.setLastName(user.getLastName());
        paragraphDTO.setPhoneNumber(user.getPhoneNumber());
        paragraphDTO.setStartedAt(testSession.getStartedAt());
        paragraphDTO.setFinishedAt(testSession.getFinishedAt());

        if (firstSubjectId != null) {
            forPDF = test(forPDF, testSessionId, firstSubjectId);
            paragraphDTO.getScores().add(firstCount * 3.1);
        }
        if (secondSubjectId != null) {
            forPDF = test(forPDF, testSessionId, secondSubjectId);
            paragraphDTO.getScores().add(secondCount * 2.1);
        }
        if (thirdSubjectId != null) {
            forPDF = test(forPDF, testSessionId, thirdSubjectId);
            paragraphDTO.getScores().add(thirdCount * 1.1);
        }
        if (fourthSubjectId != null) {
            forPDF = test(forPDF, testSessionId, fourthSubjectId);
            paragraphDTO.getScores().add(fourthCount * 1.1);
        }
        if (fifthSubjectId != null) {
            forPDF = test(forPDF, testSessionId, fifthSubjectId);
            paragraphDTO.getScores().add(fifthCount * 1.1);
        }

        File file = generatePDF.generatePDF(paragraphDTO, forPDF);
//        Document document = documentService.saveDocument(file);
        return CompletableFuture.completedFuture(new FileCreatedEvent(file, testSessionId));
    }

    private List<TestHistoryPDFGenerateDTO> test(List<TestHistoryPDFGenerateDTO> forPDF, Integer testSessionId, Integer subjectId) {
        if (subjectId != null) {
            String fifthSubjectName = subjectService.getSubjectResultDTOById(subjectId).getName();
            List<Boolean> listOfAnswerTrueOrFalse = solveQuestionService.getListOfAnswerTrueOrFalse(testSessionId, subjectId);
            TestHistoryPDFGenerateDTO fifthTestHistoryPDFGenerateDTO = new TestHistoryPDFGenerateDTO(fifthSubjectName, listOfAnswerTrueOrFalse);
            forPDF.add(fifthTestHistoryPDFGenerateDTO);
        }
        return forPDF;
    }

}
