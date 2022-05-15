package ru.valkov.carsharing.carsharingapplication.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken " + "SET confirmedAt = ?2 " + "WHERE id = ?1")
    void updateConfirmedAtById(Long id, LocalDateTime confirmedAt);

    List<ConfirmationToken> findAllByAppUserId(Long userId);
}
