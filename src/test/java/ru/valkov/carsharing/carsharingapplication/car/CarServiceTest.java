package ru.valkov.carsharing.carsharingapplication.car;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.valkov.carsharing.carsharingapplication.ui.dto.CarRequest;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserRepository;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class CarServiceTest {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CarService carService;

    @Test
    void saveUserAndRentCar() {
        AppUser user  = new AppUser();
        user.setEmail("email@mail.ru");
        user.setName("Name");
        user.setPhoneNumber("1092842189-948");
        user.setPassword("pass");
        appUserRepository.save(user);

        Long ownerId = 1L;
        CarRequest car = new CarRequest();
        car.setLongitude(110.0);
        car.setLatitude(110.0);
        car.setPricePerMinute(BigDecimal.TEN);
        car.setName("Ferrari");
        car.setCarNumber("111");

        carService.save(ownerId, car);


        Car carById = carService.getById(1L);
        assertThat(carById).isNotNull();
    }
}