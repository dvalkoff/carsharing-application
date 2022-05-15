package ru.valkov.carsharing.carsharingapplication.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.valkov.carsharing.carsharingapplication.car.Car;
import ru.valkov.carsharing.carsharingapplication.car.CarService;
import ru.valkov.carsharing.carsharingapplication.ui.dto.FeatureCollection;

import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Objects.isNull;

@Controller
@RequestMapping(path = "/ui")
@RequiredArgsConstructor
@Slf4j
public class UIController {
    private final CarService carService;
    private final UIService uiService;

    @GetMapping
    public String home(Model model,
                       @RequestParam(required = false) Double latitude,
                       @RequestParam(required = false) Double longitude,
                       Authentication authentication) {
        if (uiService.hasNoRentedCarsByEmail(authentication.getName())) {
            model.addAttribute(
                    "cars",
                    (isNull(latitude) || isNull(longitude))
                            ? Collections.emptyList()
                            : carService.findClosestCarsByCoordinates(latitude, longitude));
            return "index";
        } else {
            Car car = uiService.getRentedCar(authentication);
            LocalDateTime rentedFrom = car.getRentedFrom();
            model.addAttribute("currentTime", rentedFrom.toString());
            model.addAttribute("pricePerMinute", car.getPricePerMinute());
            model.addAttribute("carName", car.getName());
            model.addAttribute("carNums", car.getCarNumber());
            return "rent";
        }
    }

    @GetMapping(path = "/map")
    public ResponseEntity<FeatureCollection> getMapMarkers(@RequestParam Double latitude,
                                                           @RequestParam Double longitude) {
        return ResponseEntity.ok(uiService.getClosestMarkers(latitude, longitude));
    }

    @GetMapping(path = "/cars/{carId}/rent")
    public RedirectView rent(@PathVariable Long carId, @Autowired Authentication authentication) {
        carService.rent(authentication.getName(), carId);
        return new RedirectView("/ui");
    }

    @GetMapping("/user/rent")
    public RedirectView completeRent(Authentication authentication, @RequestParam Double latitude, @RequestParam Double longitude) {
        carService.completeTheRent(authentication.getName(), latitude, longitude);
        return new RedirectView("/ui");
    }
}
