package uz.test.abitur.events_listeners.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import uz.test.abitur.domains.Document;
import uz.test.abitur.events_listeners.events.DocumentSavedEvent;
import uz.test.abitur.services.DocumentService;
import uz.test.abitur.services.firebase.FirebaseService;

import java.io.File;
import java.io.IOException;

@Component
@EnableAsync
@RequiredArgsConstructor
public class DocumentSavedEventListener {
    private final FirebaseService firebaseService;
    private final DocumentService documentService;

    @Async
    @EventListener(value = DocumentSavedEvent.class)
    public void documentSavedEventListener(DocumentSavedEvent event) throws IOException {
        File file = event.getFile();
        Document document = event.getDocument();
        String path = firebaseService.upload(file, document.getGeneratedName());
        document.setFilePath(path);
        documentService.update(document);
    }
}
