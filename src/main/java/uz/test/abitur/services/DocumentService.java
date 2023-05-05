package uz.test.abitur.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import uz.test.abitur.domains.Document;
import uz.test.abitur.repositories.DocumentRepository;
import uz.test.abitur.services.firebase.FirebaseService;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static uz.test.abitur.utils.BaseUtils.generateFileName;
import static uz.test.abitur.utils.BaseUtils.getExtension;


@Service
@RequiredArgsConstructor
public class DocumentService {
    private final FirebaseService firebaseService;
    private final DocumentRepository documentRepository;
    private final ApplicationEventPublisher publisher;

    public Document saveDocument(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(Objects.requireNonNull(originalFilename));
        String generatedName = generateFileName() + extension;
        Document document = Document.builder()
                .originalName(originalFilename)
                .generatedName(generatedName)
                .extension(extension)
                .contentType(file.getContentType())
                .size(file.getSize())
                .filePath(firebaseService.upload(file, generatedName))
                .build();
        documentRepository.save(document);
        return document;
    }

    public Document saveDocument(File file) {
        String originalFilename = file.getName();
        String extension = getExtension(Objects.requireNonNull(originalFilename));
        String generatedName = generateFileName() + extension;
        Document document = Document.builder()
                .originalName(originalFilename)
                .generatedName(generatedName)
                .extension(extension)
                .contentType(MimeType.valueOf("application/pdf").getType())
                .size(file.length())
                .build();
        documentRepository.save(document);
        return document;
    }

  /*  @Cacheable(value = "fileCache", key = "#name")*/
    public File downloadFile(String name) throws IOException {
        return firebaseService.download(name);
    }

    /*@Cacheable(value = "documentCache", key = "#generatedName")*/
    public Document getByGeneratedName(String generatedName) {
        return documentRepository.findByGeneratedName(generatedName).orElseThrow(() -> new RuntimeException("Document Not found"));
    }

    /*@CachePut(value = "documentCache", key = "#document.generatedName")*/
    public Document update(Document document) {
        return documentRepository.save(document);
    }
}
