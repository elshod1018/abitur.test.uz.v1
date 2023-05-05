package uz.test.abitur.events_listeners.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uz.test.abitur.dtos.test.TestSessionResultDTO;
@RequiredArgsConstructor
@Getter
public final class TestSessionStartedEvent {
    private final TestSessionResultDTO testSessionResultDTO;
}
