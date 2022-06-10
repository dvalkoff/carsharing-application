package ru.valkov.carsharing.carsharingapplication.registration.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor
@Setter
public class ConfirmationToken {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence_id"
    )
    @SequenceGenerator(
            name = "confirmation_token_sequence_id",
            sequenceName = "confirmation_token_sequence_id",
            allocationSize = 1
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
