package ru.valkov.carsharing.carsharingapplication.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.valkov.carsharing.carsharingapplication.car.CarService;

import java.util.Collections;

import static java.util.Objects.isNull;

@Controller
@RequestMapping(path = "/ui")
@RequiredArgsConstructor
public class UIController {
    private final CarService carService;

    @GetMapping
    public String home(Model model, @RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude) {
        if (isNull(latitude) || isNull(longitude)) {
            model.addAttribute("cars", Collections.emptyList());
        } else {
            model.addAttribute("cars", carService.findClosestCarsByCoordinates(latitude, longitude));
        }
        return "index";
    }
}
