package ru.valkov.carsharing.carsharingapplication.car;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {
    private final CarRepository carRepository;
    private final AppUserService appUserService;

    public void save(Long ownerId, Car car) {
        AppUser owner = appUserService.getById(ownerId);
        car.setId(null);
        car.setCurrentRenter(null);
        car.setOwner(owner);
        car.setCarState(CarState.SERVED);
        car.setRentedFrom(null);
        carRepository.save(car);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }
}
