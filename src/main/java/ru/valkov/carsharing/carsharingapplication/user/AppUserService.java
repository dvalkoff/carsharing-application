package ru.valkov.carsharing.carsharingapplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkov.carsharing.carsharingapplication.email.EmailSender;
import ru.valkov.carsharing.carsharingapplication.email.EmailUtils;
import ru.valkov.carsharing.carsharingapplication.registration.token.ConfirmationToken;
import ru.valkov.carsharing.carsharingapplication.registration.token.ConfirmationTokenService;
import ru.valkov.carsharing.carsharingapplication.shared.exceptions.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    @Value("${server.url}")
    private String baseUrl;

    public AppUser getById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void save(AppUser appUser) {
        Optional<AppUser> unexpectedUser = appUserRepository.findByEmail(appUser.getUsername());

        String token = UUID.randomUUID().toString();
        String linkToConfirmToken = baseUrl + "/sign-up/confirm?token=" + token;

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15)
        );

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        if (unexpectedUser.isEmpty()) {
            AppUser savedUser = appUserRepository.save(appUser);
            confirmationToken.setAppUser(savedUser);
        } else {
            if (unexpectedUser.get().isEnabled()) {
                throw new IllegalStateException(String.format("User with email %s already exists", appUser.getUsername()));
            } else {
                if (anyTokenNotExpired(unexpectedUser.get())) {
                    throw new IllegalStateException(String.format("User with email %s already exists", appUser.getUsername()));
                } else {
                    confirmationToken.setAppUser(unexpectedUser.get());
                }
            }
        }

        if (unexpectedUser.isEmpty()) {
            AppUser savedUser = appUserRepository.save(appUser);
            confirmationToken.setAppUser(savedUser);
        } else if (unexpectedUser.get().isEnabled()
                || anyTokenNotExpired(unexpectedUser.get())) {
            throw new IllegalStateException(String.format("User with email %s already exists", appUser.getUsername()));
        }
        confirmationToken.setAppUser(unexpectedUser.get());



        confirmationTokenService.saveConfirmationToken(confirmationToken);

        emailSender.sendEmail(appUser.getUsername(), EmailUtils.buildEmail(linkToConfirmToken));
    }

    private boolean anyTokenNotExpired(AppUser notExpectedUser) {
        List<ConfirmationToken> confirmationTokens = confirmationTokenService.getTokensByUserId(notExpectedUser.getId());
        boolean anyTokenNotExpired = false;
        for (ConfirmationToken token: confirmationTokens) {
            if (token.getExpiresAt().isAfter(LocalDateTime.now())) {
                anyTokenNotExpired = true;
            }
        }
        return anyTokenNotExpired;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalStateException(String.format("User with username %s not found", username)));
    }

    @Transactional
    public void confirmAccount(String token) throws IllegalStateException {
        ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenByToken(token);

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token already expired");
        }

        AppUser appUser = appUserRepository.findById(confirmationToken.getAppUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        confirmationTokenService.updateConfirmedAt(confirmationToken.getId(), LocalDateTime.now());

        appUserRepository.updateEnabledById(appUser.getId(), true);
    }
}
