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
@Table(name = "user_sms")
@ToString
public class UserSMS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @CreationTimestamp
    @Column(name = "from_time", columnDefinition = "timestamp default now()", updatable = false)
    private LocalDateTime fromTime;

    @Builder.Default
    @Column(name = "to_time",columnDefinition = "timestamp default now()+INTERVAL '2 Minutes'")
    private LocalDateTime toTime = LocalDateTime.now().plusMinutes(2);

    @Column(name = "is_expired")
    private boolean expired;
}
