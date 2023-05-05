package uz.test.abitur.controller.document;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.test.abitur.domains.Document;
import uz.test.abitur.services.DocumentService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

//    @PostMapping("/upload")
//    public ResponseEntity<Document> uploadFile(MultipartFile file) throws IOException {
//        Document document = documentService.saveDocument(file);
//        return ResponseEntity.ok(document);
//    }

    @GetMapping("/download/{name:.*}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String name) throws IOException {
        File file = documentService.downloadFile(name);
        Document document = documentService.getByGeneratedName(name);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getOriginalName())
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
