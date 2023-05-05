package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.TestHistory;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.events_listeners.events.TestHistoryFinishedEvent;
import uz.test.abitur.repositories.TestHistoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestHistoryService {
    private final TestHistoryRepository testHistoryRepository;
    private final SolveQuestionService solveQuestionService;
    private final SubjectService subjectService;
    private final UserSession userSession;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TestHistory create(TestSession testSession) {
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
        Double totalScore = (firstCount * 3.1 + secondCount * 2.1 + (thirdCount + fourthCount + fifthCount) * 1.1);

        return testHistoryRepository.save(
                TestHistory.builder()
                        .userId(testSession.getUserId())
                        .testSessionId(testSessionId)
                        .startedAt(testSession.getStartedAt())
                        .finishedAt(testSession.getFinishedAt())
                        .firstSubject(firstSubjectId != null ? subjectService.getSubjectResultDTOById(firstSubjectId).getName() + "  " + firstCount + "/30" : "")
                        .secondSubject(secondSubjectId != null ? subjectService.getSubjectResultDTOById(secondSubjectId).getName() + "  " + secondCount + "/30" : "")
                        .thirdSubject(thirdSubjectId != null ? subjectService.getSubjectResultDTOById(thirdSubjectId).getName() + "  " + thirdCount + "/10" : "")
                        .fourthSubject(fourthSubjectId != null ? subjectService.getSubjectResultDTOById(fourthSubjectId).getName() + "  " + fourthCount + "/10" : "")
                        .fifthSubject(fifthSubjectId != null ? subjectService.getSubjectResultDTOById(fifthSubjectId).getName() + "  " + fifthCount + "/10" : "")
                        .totalScore(totalScore)
                        .build()
        );
    }

   /* @CachePut(cacheNames = "testHistoryByTestSessionId", key = "#testSession.id")*/
    public TestHistory finish(TestSession testSession) {
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
        Double firstScore = firstCount * 3.1;
        Double secondScore = secondCount * 2.1;
        Double thirdScore = thirdCount * 1.1;
        Double fourthScore = fourthCount * 1.1;
        Double fifthScore = fifthCount * 1.1;
        Double totalScore = firstScore + secondScore + thirdScore + fourthScore + fifthScore;

        TestHistory testHistory = testHistoryRepository.findByTestSessionId(testSessionId);
        testHistory.setFinishedAt(testSession.getFinishedAt());

        if (firstSubjectId != null) {
            String firstSubjectName = subjectService.getSubjectResultDTOById(firstSubjectId).getName();
            testHistory.setFirstSubject(firstSubjectName + "  " + firstCount + "/30");
        }
        if (secondSubjectId != null) {
            String secondSubjectName = subjectService.getSubjectResultDTOById(secondSubjectId).getName();
            testHistory.setSecondSubject(secondSubjectName + "  " + secondCount + "/30");
        }
        if (thirdSubjectId != null) {
            String thirdSubjectName = subjectService.getSubjectResultDTOById(thirdSubjectId).getName();
            testHistory.setThirdSubject(thirdSubjectName + "  " + thirdCount + "/10");
        }
        if (fourthSubjectId != null) {
            String fourthSubjectName = subjectService.getSubjectResultDTOById(fourthSubjectId).getName();
            testHistory.setFourthSubject(fourthSubjectName + "  " + fourthCount + "/10");
        }
        if (fifthSubjectId != null) {
            String fifthSubjectName = subjectService.getSubjectResultDTOById(fifthSubjectId).getName();
            testHistory.setFifthSubject(fifthSubjectName + "  " + fifthCount + "/10");
        }
        testHistory.setTotalScore(totalScore);
        testHistoryRepository.save(testHistory);
        applicationEventPublisher.publishEvent(new TestHistoryFinishedEvent(testSession));
        return testHistory;
    }

    public List<TestHistory> userAllHistory(String userId) {
        return testHistoryRepository.findAll(userId);
    }

    /*@Cacheable(value = "testHistoryByTestSessionId", key = "#testSessionId")*/
    public TestHistory findByTestSessionId(Integer testSessionId) {
        return testHistoryRepository.findByTestSessionId(testSessionId);
    }

   /* @CachePut(value = "testHistoryByTestSessionId", key = "#testHistory.testSessionId")*/
    public TestHistory update(TestHistory testHistory) {
        return testHistoryRepository.save(testHistory);
    }

    public List<TestHistory> getAllHistory() {
        return testHistoryRepository.findAll();
    }
}
