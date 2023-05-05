package uz.test.abitur.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import uz.test.abitur.enums.Language;
import uz.test.abitur.enums.Role;
import uz.test.abitur.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class AuthUser {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(name = "profile_photo_generated_name")
    private String profilePhotoGeneratedName;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Language language = Language.UZ;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Status status = Status.NO_ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "timestamp default now()", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean deleted;

}
