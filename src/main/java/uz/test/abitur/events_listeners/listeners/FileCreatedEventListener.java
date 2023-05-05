package uz.test.abitur.events_listeners.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import uz.test.abitur.domains.Document;
import uz.test.abitur.domains.TestHistory;
import uz.test.abitur.events_listeners.events.DocumentSavedEvent;
import uz.test.abitur.events_listeners.events.FileCreatedEvent;
import uz.test.abitur.services.DocumentService;
import uz.test.abitur.services.TestHistoryService;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@EnableAsync
@RequiredArgsConstructor
public class FileCreatedEventListener {

    private final DocumentService documentService;
    private final TestHistoryService testHistoryService;

    @Async
    @EventListener(value = FileCreatedEvent.class
           /* condition = "#event.file ne null",*/
            )
    public CompletableFuture<DocumentSavedEvent> fileCreatedEventListener(FileCreatedEvent event) {
        File file = event.getFile();
        Document document = documentService.saveDocument(file);

        TestHistory testHistory = testHistoryService.findByTestSessionId(event.getTestSessionId());
        if (!Objects.isNull(testHistory)) {
            testHistory.setDocumentId(document.getGeneratedName());
            testHistoryService.update(testHistory);
        }
        return CompletableFuture.completedFuture(new DocumentSavedEvent(document, file));
    }


}
