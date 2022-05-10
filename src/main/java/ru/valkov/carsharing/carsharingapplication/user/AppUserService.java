package ru.valkov.carsharing.carsharingapplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.shared.exceptions.NotFoundException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser getById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void save(AppUser appUser) {
        appUser.setId(null);
        appUser.setCars(null);
        appUser.setRentedCar(null);
        appUser.setCashAccount(new BigDecimal("1000"));
        appUserRepository.save(appUser);
    }
}
