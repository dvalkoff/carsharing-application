package ru.valkov.carsharing.carsharingapplication.car;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.shared.exceptions.BadRequestException;
import ru.valkov.carsharing.carsharingapplication.shared.exceptions.NotFoundException;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarService {
    private final CarRepository carRepository;
    private final AppUserService appUserService;

    // basic CRUD

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car getById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new NotFoundException("Car not found"));
    }

    // business logic
    public List<Car> findClosestCarsByCoordinates(Double latitude, Double longitude) {
        log.info("Trying to get closest cars to latitude = {} and longitude = {}", latitude, longitude);
        return carRepository.findClosest(latitude, longitude);
    }


    @Transactional
    public void rent(String email, Long carId) {
        AppUser renter = appUserService.loadUserByUsername(email);
        if (renter.getCashAccount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("User has not enough valkoins to rent a car");
        }
        if (nonNull(renter.getRentedCar())) {
            throw new BadRequestException("User can not rent two cars simultaneously");
        }
        Car carToRent = this.getById(carId);
        if (carToRent.getCarState() != CarState.FREE) {
            throw new BadRequestException("Car can not be rented tight now. Try later");
        }
        carToRent.setCarState(CarState.IS_TAKEN);
        carToRent.setCurrentRenter(renter);
        carToRent.setRentedFrom(LocalDateTime.now(Clock.systemUTC()));
        carRepository.save(carToRent);
    }

    @Transactional
    public void completeTheRent(Long renterId, Long carId) {
        Car takenCar = this.getById(carId);
        AppUser renter = takenCar.getCurrentRenter();
        if (isNull(renter) || !renterId.equals(renter.getId())) {
            throw new BadRequestException("Specified user did not rent this car");
        }
        if (takenCar.getCarState() != CarState.IS_TAKEN) {
            throw new BadRequestException("Can not complete the rent. Car is not taken.");
        }
        calculateAndWithdrawAndCreditPayment(renter, takenCar);
        takenCar.setCarState(CarState.FREE);
        takenCar.setCurrentRenter(null);
        takenCar.setRentedFrom(null);
    }

    @Transactional
    public void completeTheRent(String renterEmail, Double latitude, Double longitude) {
        AppUser renter = appUserService.loadUserByUsername(renterEmail);
        Car takenCar = renter.getRentedCar();

        if (takenCar.getCarState() != CarState.IS_TAKEN) {
            throw new BadRequestException("Can not complete the rent. Car is not taken.");
        }
        calculateAndWithdrawAndCreditPayment(renter, takenCar);
        takenCar.setCarState(CarState.FREE);
        takenCar.setLatitude(latitude);
        takenCar.setLongitude(longitude);
        takenCar.setCurrentRenter(null);
        takenCar.setRentedFrom(null);
    }

    private void calculateAndWithdrawAndCreditPayment(AppUser renter, Car takenCar) {
        LocalDateTime rentedFrom = takenCar.getRentedFrom();
        LocalDateTime rentedTo = LocalDateTime.now(Clock.systemUTC());

        BigDecimal secondsSpentInTrip = new BigDecimal(ChronoUnit.SECONDS.between(rentedFrom, rentedTo));
        BigDecimal minutesSpentInTrip = secondsSpentInTrip.divide(new BigDecimal(60), RoundingMode.UP);
        BigDecimal payment = takenCar.getPricePerMinute().multiply(minutesSpentInTrip);

        AppUser carOwner = renter.getRentedCar().getOwner();
        renter.setCashAccount(renter.getCashAccount().subtract(payment));
        carOwner.setCashAccount(carOwner.getCashAccount().add(payment));
    }

    @Transactional
    public void save(Long ownerId, Car car) {
        AppUser owner = appUserService.getById(ownerId);
        car.setId(null);
        car.setCurrentRenter(null);
        car.setRentedFrom(null);
        car.setOwner(owner);
        car.setCarState(CarState.SERVED);
        carRepository.save(car);
    }

    @Transactional
    public void giveBackCarToOwner(Long ownerId, Long carId) {
        Car car = this.getById(carId);
        AppUser owner = car.getOwner();
        if (!owner.getId().equals(ownerId)) {
            throw new BadRequestException("User can not get back someone else's car");
        }
        if (car.getCarState() != CarState.FREE) {
            throw new BadRequestException("User can not get back someone else's car");
        }
        car.setCarState(CarState.SERVED);
        carRepository.save(car);
    }
}
