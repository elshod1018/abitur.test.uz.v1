package uz.test.abitur.events_listeners.events;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import uz.test.abitur.domains.Document;

import java.io.File;

@Getter
@RequiredArgsConstructor
public final class DocumentSavedEvent {
    private final Document document;
    private final File file;
}
