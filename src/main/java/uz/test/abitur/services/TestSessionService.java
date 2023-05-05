package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import uz.test.abitur.config.security.UserSession;
import uz.test.abitur.domains.Subject;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.dtos.subject.SubjectResultDTO;
import uz.test.abitur.dtos.subject.SubjectShowDTO;
import uz.test.abitur.dtos.test.TestSessionCreateDTO;
import uz.test.abitur.dtos.test.TestSessionResultDTO;
import uz.test.abitur.events_listeners.events.TestSessionFinishedEvent;
import uz.test.abitur.repositories.TestSessionRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestSessionService {
    private final TestSessionRepository testSessionRepository;
    private final UserSession userSession;
    private final SubjectService subjectService;
    private final TestHistoryService testHistoryService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TestSessionResultDTO create(TestSessionCreateDTO dto) {
        Integer firstSubjectId = dto.getFirstSubjectId();
        Integer secondSubjectId = dto.getSecondSubjectId();

        TestSession testSession = new TestSession();
        testSession.setUserId(userSession.getId());
        testSession.setStartedAt(LocalDateTime.now());
        testSession.setFinishedAt(LocalDateTime.now());

        TestSessionResultDTO resultDTO = new TestSessionResultDTO();

        if (firstSubjectId != null && firstSubjectId != -1) {
            testSession.setFirstSubjectId(firstSubjectId);
            testSession.setFinishedAt(testSession.getFinishedAt().plusMinutes(60));
            SubjectResultDTO byId = subjectService.getSubjectResultDTOById(firstSubjectId);
            resultDTO.setFirstSubject(SubjectShowDTO.builder()
                    .id(byId.getId())
                    .name(byId.getName())
                    .mandatory(false)
                    .build());
        }
        if (secondSubjectId != null && secondSubjectId != -1) {
            testSession.setSecondSubjectId(secondSubjectId);
            testSession.setFinishedAt(testSession.getFinishedAt().plusMinutes(60));
            SubjectResultDTO byId = subjectService.getSubjectResultDTOById(secondSubjectId);
            resultDTO.setSecondSubject(SubjectShowDTO.builder()
                    .id(byId.getId())
                    .name(byId.getName())
                    .mandatory(false)
                    .build());
        }
        if (dto.isMandatory()) {
            List<Subject> mandatoryList = subjectService.getMandatory();
            Integer thirdSubjectId = mandatoryList.get(0).getId();
            Integer fourthSubjectId = mandatoryList.get(1).getId();
            Integer fifthSubjectId = mandatoryList.get(2).getId();

            testSession.setThirdSubjectId(thirdSubjectId);
            testSession.setFourthSubjectId(fourthSubjectId);
            testSession.setFifthSubjectId(fifthSubjectId);
            testSession.setFinishedAt(testSession.getFinishedAt().plusMinutes(60));

            SubjectResultDTO third = subjectService.getSubjectResultDTOById(thirdSubjectId);
            SubjectResultDTO fourth = subjectService.getSubjectResultDTOById(fourthSubjectId);
            SubjectResultDTO fifth = subjectService.getSubjectResultDTOById(fifthSubjectId);

            resultDTO.setThirdSubject(SubjectShowDTO.builder()
                    .id(third.getId())
                    .name(third.getName())
                    .mandatory(true)
                    .build());

            resultDTO.setFourthSubject(SubjectShowDTO.builder()
                    .id(fourth.getId())
                    .name(fourth.getName())
                    .mandatory(true)
                    .build());

            resultDTO.setFifthSubject(SubjectShowDTO.builder()
                    .id(fifth.getId())
                    .name(fifth.getName())
                    .mandatory(true)
                    .build());
        }
        testSessionRepository.save(testSession);
        resultDTO.setId(testSession.getId());
        return resultDTO;
    }

    public void finish(Integer testSessionId) throws IOException {
        TestSession testSession = testSessionRepository.findById(userSession.getId(), testSessionId)
                .orElseThrow(() -> new RuntimeException("Test session not found"));
        testSession.setFinished(true);
        testSession.setFinishedAt(LocalDateTime.now());
        testSessionRepository.save(testSession);
        applicationEventPublisher.publishEvent(new TestSessionFinishedEvent(testSession));
    }

    public TestSession findActiveTestSession() {
        Optional<TestSession> testSession = testSessionRepository.findActive(userSession.getId());
        return testSession.orElse(null);
    }

    public TestSession findActiveTestSession(Integer id) {
        Optional<TestSession> testSession = testSessionRepository.findById(id);
        return testSession.orElse(null);
    }

    public TestSessionResultDTO findById(Integer id) {
        Optional<TestSession> optional = testSessionRepository.findById(id);
        if (optional.isPresent()) {
            TestSession testSession = optional.get();
            if (!testSession.getUserId().equals(userSession.getId())) {
                throw new RuntimeException("Test session not found");
            }
            if (testSession.getFinishedAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Test session not found");
            }
            TestSessionResultDTO resultDTO = new TestSessionResultDTO();
            resultDTO.setId(testSession.getId());
            Integer firstSubjectId = testSession.getFirstSubjectId();
            Integer secondSubjectId = testSession.getSecondSubjectId();
            Integer thirdSubjectId = testSession.getThirdSubjectId();
            Integer fifthSubjectId = testSession.getFifthSubjectId();
            Integer fourthSubjectId = testSession.getFourthSubjectId();
            if (firstSubjectId != null) {
                resultDTO.setFirstSubject(subjectService.getSubjectShowDTOById(firstSubjectId));
            }
            if (secondSubjectId != null) {
                resultDTO.setSecondSubject(subjectService.getSubjectShowDTOById(secondSubjectId));
            }
            if (thirdSubjectId != null) {
                resultDTO.setThirdSubject(subjectService.getSubjectShowDTOById(thirdSubjectId));
            }
            if (fourthSubjectId != null) {
                resultDTO.setFourthSubject(subjectService.getSubjectShowDTOById(fourthSubjectId));
            }
            if (fifthSubjectId != null)
                resultDTO.setFifthSubject(subjectService.getSubjectShowDTOById(fifthSubjectId));
            return resultDTO;
        }
        throw new RuntimeException("Test session not found");
    }

    public List<TestSession> findUnfinishedTestSession() {
        return testSessionRepository.findUnfinishedTestSession();
    }
}
