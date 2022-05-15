package ru.valkov.carsharing.carsharingapplication.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.valkov.carsharing.carsharingapplication.car.Car;
import ru.valkov.carsharing.carsharingapplication.car.CarService;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Feature;
import ru.valkov.carsharing.carsharingapplication.ui.dto.FeatureCollection;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Geometry;
import ru.valkov.carsharing.carsharingapplication.ui.dto.Properties;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UIService {
    private final CarService carService;

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
        properties.setBalloonContentBody("<p>Owner: " + car.getOwner().getName() + "</p><p>Price per minute: " + car.getPricePerMinute() + "<img th:src=\"@{images/bitcoin.svg}\" height=\"40\"></p>");
        properties.setBalloonContentFooter("<button>Rent the car</button>");
        properties.setClusterCaption(car.getName());
        properties.setHintContent(car.getName());
        return properties;
    }
}
