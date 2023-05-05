package uz.test.abitur.events_listeners.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class SolveQuestionCreatedEvent {
    private final Integer testSessionId;
}
