package uz.test.abitur.services.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

import static uz.test.abitur.utils.BaseUtils.getExtension;
import static uz.test.abitur.utils.MediaUtils.BUCKET_NAME;
import static uz.test.abitur.utils.MediaUtils.DOWNLOAD_URL;


@Service
public class FirebaseService {
    @Value("${firebase.type}")
    private String type;
    @Value("${firebase.project_id}")
    private String project_id;
    @Value("${firebase.private_key_id}")
    private String private_key_id;
    @Value("${firebase.private_key}")
    private String private_key;
    @Value("${firebase.client_email}")
    private String client_email;
    @Value("${firebase.client_id}")
    private String client_id;
    @Value("${firebase.auth_uri}")
    private String auth_uri;
    @Value("${firebase.token_uri}")
    private String token_uri;
    @Value("${firebase.auth_provider_x509_cert_url}")
    private String auth_provider_x509_cert_url;
    @Value("${firebase.client_x509_cert_url}")
    private String client_x509_cert_url;
    @SneakyThrows
    public String upload(MultipartFile file, String generatedName) {
        Storage service = getStorage();
        service.create(getBlobInfo(generatedName, file.getContentType()), file.getBytes());
        return String.format(DOWNLOAD_URL, generatedName);
    }

    public String upload(File file, String generatedName) throws IOException {
        Storage service = getStorage();
        try (FileInputStream fis = new FileInputStream(file)) {
            service.create(getBlobInfo(generatedName, MimeType.valueOf("application/pdf").getType()),
                    fis.readAllBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return String.format(DOWNLOAD_URL, generatedName);
    }

    public File download(String fileName) throws IOException {
        Storage service = getStorage();
        Blob blob = service.get(BlobId.of(BUCKET_NAME, fileName));
        File file = File.createTempFile(fileName, getExtension(fileName));
        blob.downloadTo(Paths.get(file.getAbsolutePath()));
        return file;
    }

    private Storage getStorage() throws IOException {
        return StorageOptions
                .newBuilder()
                .setCredentials(getCredentials())
                .build()
                .getService();
    }

    private BlobInfo getBlobInfo(String fileName, String contentType) {
        return BlobInfo
                .newBuilder(BlobId.of(BUCKET_NAME, fileName))
                .setContentType(Objects.requireNonNullElse(contentType, "media"))
                .build();
    }

//    @SneakyThrows
//    private Credentials getCredentials() {
//        File file = new File(FIREBASE_CREDENTIALS_PATH);
//        return GoogleCredentials.fromStream(new FileInputStream(file));
//    }
    private Credentials getCredentials() throws IOException {
        return GoogleCredentials.fromStream(IOUtils.toInputStream("""
                {
                "type": "%s",
                "project_id": "%s",
                "private_key_id": "%s",
                "private_key": "%s",
                "client_email": "%s",
                "client_id": "%s",
                "auth_uri": "%s",
                "token_uri": "%s",
                "auth_provider_x509_cert_url": "%s",
                "client_x509_cert_url": "%s"
                }
                """.formatted(type,
                project_id,
                private_key_id,
                private_key,
                client_email,
                client_id,
                auth_uri,
                token_uri,
                auth_provider_x509_cert_url,
                client_x509_cert_url), StandardCharsets.UTF_8));
    }
}
