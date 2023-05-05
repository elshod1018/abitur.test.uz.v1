package uz.test.abitur.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String body;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "timestamp default now()", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean deleted;
}
