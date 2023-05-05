package uz.test.abitur.domains;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Question {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, name = "created_by", updatable = false)
    private String createdBy;

    @Column(nullable = false, name = "subject_id")
    private Integer subjectId;

    @Column(nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "timestamp default now()", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_deleted")
    private boolean deleted;
}
