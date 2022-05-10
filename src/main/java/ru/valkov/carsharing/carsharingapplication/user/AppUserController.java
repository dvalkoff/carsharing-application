package ru.valkov.carsharing.carsharingapplication.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping
    public void save(@RequestBody AppUser appUser) {
        appUserService.save(appUser);
    }

    @GetMapping(path = "/{id}")
    public AppUser getById(@PathVariable Long id) {
        return appUserService.getById(id);
    }
}
