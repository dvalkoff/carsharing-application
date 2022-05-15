package ru.valkov.carsharing.carsharingapplication.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserRole;
import ru.valkov.carsharing.carsharingapplication.user.AppUserService;

import java.math.BigDecimal;

@Service
public class SignService {
    private final AppUserService appUserService;

    @Autowired
    public SignService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public void registerUser(AppUserDetailsRequest request) {
        AppUser appUser = AppUser
                .builder()
                .email(request.getEmail())
                .name(request.getName())
                .cashAccount(BigDecimal.ZERO)
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .userRole(AppUserRole.USER)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(false)
                .build();
        appUserService.save(appUser);
    }

    public void confirmAccount(String token) throws IllegalStateException {
        appUserService.confirmAccount(token);
    }

}
