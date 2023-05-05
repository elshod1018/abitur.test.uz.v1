package uz.test.abitur.events_listeners.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import uz.test.abitur.domains.TestSession;
import uz.test.abitur.dtos.test.TestSessionResultDTO;
import uz.test.abitur.events_listeners.events.SolveQuestionCreatedEvent;
import uz.test.abitur.events_listeners.events.TestSessionFinishedEvent;
import uz.test.abitur.events_listeners.events.TestSessionStartedEvent;
import uz.test.abitur.services.SolveQuestionService;
import uz.test.abitur.services.TestHistoryService;
import uz.test.abitur.services.TestSessionService;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
@RequiredArgsConstructor
public class TestSessionEventListener {
    private final SolveQuestionService solveQuestionService;
    private final TestSessionService testSessionService;
    private final TestHistoryService testHistoryService;

    @Async
    @EventListener(value = TestSessionStartedEvent.class
            /*condition = "#event.testSessionResultDTO ne null",*/
            )
    public CompletableFuture<SolveQuestionCreatedEvent> testSessionStartedEventListener(TestSessionStartedEvent event) {
        TestSessionResultDTO testSessionResultDTO = event.getTestSessionResultDTO();
        if (!Objects.isNull(testSessionResultDTO)) {
            solveQuestionService.create(testSessionResultDTO);
            return CompletableFuture.completedFuture(new SolveQuestionCreatedEvent(testSessionResultDTO.getId()));
        }
        return null;
    }

    @Async
    @EventListener(value = SolveQuestionCreatedEvent.class
            /*condition = "#event.testSessionId ne null",*/
            )
    public void solveQuestionCreatedEventListener(SolveQuestionCreatedEvent event) {
        Integer testSessionId = event.getTestSessionId();
        TestSession testSession = testSessionService.findActiveTestSession(testSessionId);
        if (!Objects.isNull(testSession)) {
            testHistoryService.create(testSession);
        }
    }

    @Async
    @EventListener(value = TestSessionFinishedEvent.class
            /*condition = "#event.testSession ne null",*/
            )
    public void testSessionFinishedEventListener(TestSessionFinishedEvent event) {
        TestSession testSession = event.getTestSession();
        testHistoryService.finish(testSession);
    }


}
