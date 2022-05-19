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
import ru.valkov.carsharing.carsharingapplication.ui.dto.CarRequest;
import ru.valkov.carsharing.carsharingapplication.ui.dto.FeatureCollection;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserService;

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
    private final AppUserService appUserService;

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

    @GetMapping("/me")
    public String myAccount(Model model, Authentication authentication) {
        AppUser appUser = appUserService.loadUserByUsername(authentication.getName());
        model.addAttribute("appUser", appUser);
        model.addAttribute("cars", appUser.getCars());
        return "my-account";
    }


    @GetMapping("/cars/new")
    public String addCar() {
        return "add-car";
    }

    @PostMapping("/cars/new")
    public String addCar(Authentication authentication, CarRequest car) {
        AppUser appUser = appUserService.loadUserByUsername(authentication.getName());
        carService.save(appUser.getId(), car);
        return "redirect:/ui/me";
    }

    @DeleteMapping(path = "/cars/{carId}")
    public String deleteCarById(Authentication authentication, @PathVariable Long carId, Model model) {
        AppUser appUser = appUserService.loadUserByUsername(authentication.getName());
        carService.giveBackCarToOwner(appUser, carId);
        model.addAttribute("appUser", appUser);
        model.addAttribute("cars", appUser.getCars());
        return "my-account";
    }
}
