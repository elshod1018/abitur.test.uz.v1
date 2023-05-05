package uz.test.abitur.domains;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Document {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, name = "original_name")
    private String originalName;

    @Column(nullable = false, unique = true, name = "generated_name")
    private String generatedName;

    @Column(nullable = false, name = "content_type")
    private String contentType;

    @Column(nullable = false)
    private long size;

    @Column(name = "file_path")
    private String filePath;

    @Column(nullable = false)
    private String extension;
}
