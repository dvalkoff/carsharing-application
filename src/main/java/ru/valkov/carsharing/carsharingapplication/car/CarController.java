package ru.valkov.carsharing.carsharingapplication.car;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping(
            path = "/{ownerId}/cars"
    )
    public void save(@PathVariable Long ownerId, @RequestBody Car car) {
        carService.save(ownerId, car);
    }

    @GetMapping(path = "/cars")
    public List<Car> getAll() {
        return carService.findAll();
    }
}
