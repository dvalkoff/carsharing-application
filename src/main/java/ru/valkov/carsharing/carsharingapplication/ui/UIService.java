package ru.valkov.carsharing.carsharingapplication.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.car.Car;
import ru.valkov.carsharing.carsharingapplication.car.CarService;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Feature;
import ru.valkov.carsharing.carsharingapplication.ui.dto.FeatureCollection;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Geometry;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Properties;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;
import ru.valkov.carsharing.carsharingapplication.user.AppUserService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UIService {
    private final CarService carService;
    private final AppUserService appUserService;

    public FeatureCollection getClosestMarkers(Double latitude, Double longitude) {
        List<Car> cars = carService.findClosestCarsByCoordinates(latitude, longitude);
        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.setType("FeatureCollection");
        AtomicLong id = new AtomicLong(0L);
        List<Feature> features = cars
                .stream()
                .map(car -> mapToFeature(id.getAndIncrement(), car))
                .toList();
        featureCollection.setFeatures(features);
        return featureCollection;
    }

    private Feature mapToFeature(long id, Car car) {
        Feature feature = new Feature();
        feature.setType("Feature");
        feature.setId(id);
        feature.setGeometry(getGeometry(car));
        feature.setProperties(getProperties(car));
        return feature;
    }

    private Geometry getGeometry(Car car) {
        Geometry geometry = new Geometry();
        geometry.setType("Point");
        geometry.setCoordinates(List.of(car.getLatitude(), car.getLongitude()));
        return geometry;
    }

    private Properties getProperties(Car car) {
        Properties properties = new Properties();
        properties.setBalloonContentHeader("<font size=3><b>" + car.getName() + "</b></font>");
        properties.setBalloonContentBody("<p>Owner: " + car.getOwner().getName() + "</p><p>Price per minute: " + car.getPricePerMinute() + "<img src=\"images/bitcoin.svg\" height=\"40\"></p>");
        properties.setBalloonContentFooter("<a class=\"btn btn-info\" role=\"button\" href=\"/ui/cars/" + car.getId() + "/rent\">Rent the car</a>");
        properties.setClusterCaption(car.getName());
        properties.setHintContent(car.getName());
        return properties;
    }

    public boolean hasNoRentedCarsByEmail(String email) {
        AppUser appUser = appUserService.loadUserByUsername(email);
        return isNull(appUser.getRentedCar());
    }

    public Car getRentedCar(Authentication authentication) {
        AppUser appUser = appUserService.loadUserByUsername(authentication.getName());
        return appUser.getRentedCar();
    }
}
