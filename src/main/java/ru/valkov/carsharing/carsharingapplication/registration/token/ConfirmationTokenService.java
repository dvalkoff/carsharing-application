package ru.valkov.carsharing.carsharingapplication.registration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository tokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public ConfirmationToken getConfirmationTokenByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException(String.format("Token %s doesn't exist", token)));
    }

    public void updateConfirmedAt(Long id, LocalDateTime localDateTime) {
        tokenRepository.updateConfirmedAtById(id, localDateTime);
    }


    public ConfirmationToken saveConfirmationToken(ConfirmationToken confirmationToken) {
        return tokenRepository.save(confirmationToken);
    }

    public List<ConfirmationToken> getTokensByUserId(Long userId) {
        return tokenRepository.findAllByAppUserId(userId);
    }
}
