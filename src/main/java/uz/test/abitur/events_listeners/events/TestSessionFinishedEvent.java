package uz.test.abitur.events_listeners.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uz.test.abitur.domains.TestSession;

@Getter
@RequiredArgsConstructor
public final class TestSessionFinishedEvent {
    private final TestSession testSession;
}
