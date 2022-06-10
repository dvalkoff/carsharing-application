package ru.valkov.carsharing.carsharingapplication.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.valkov.carsharing.carsharingapplication.email.EmailSender;
import ru.valkov.carsharing.carsharingapplication.registration.token.ConfirmationTokenService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                AppUserService.class
        }
)
class AppUserServiceTest {
    @MockBean
    private AppUserRepository appUserRepository;
    @MockBean
    private ConfirmationTokenService confirmationTokenService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private EmailSender emailSender;

    @Autowired
    private AppUserService appUserService;

    @Test
    void getById() {
        // given
        AppUser expected = new AppUser();
        String username = "username";
        when(appUserRepository.findByEmail(username))
                .thenReturn(Optional.of(expected));
        // when
        AppUser actual = appUserService.loadUserByUsername("username");
        // then
        verify(appUserRepository).findByEmail(username);
        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void save() {
        // given
        String username = "username";
        AppUser user = new AppUser();
        user.setEmail(username);
        user.setPassword("pass");
        when(appUserRepository.findByEmail(username))
                .thenReturn(Optional.empty());
        // when
        appUserService.save(user);
        // then
        verify(appUserRepository).findByEmail(username);
        verify(passwordEncoder).encode("pass");
        verify(confirmationTokenService).saveConfirmationToken(any());
        verify(emailSender).sendEmail(eq(username), any());
    }
}