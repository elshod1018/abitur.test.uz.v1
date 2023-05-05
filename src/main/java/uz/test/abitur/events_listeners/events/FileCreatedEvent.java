package uz.test.abitur.events_listeners.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class FileCreatedEvent {
    private final File file;
    private final Integer testSessionId;
}
