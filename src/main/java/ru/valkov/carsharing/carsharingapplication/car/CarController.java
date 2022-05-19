package ru.valkov.carsharing.carsharingapplication.car;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping(path = "/cars")
    public List<Car> getAll() {
        return carService.findAll();
    }

}
