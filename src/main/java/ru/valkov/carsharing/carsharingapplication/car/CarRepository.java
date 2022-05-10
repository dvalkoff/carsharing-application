package ru.valkov.carsharing.carsharingapplication.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query(
            value = """
                    SELECT * FROM car
                    INNER JOIN app_user au on au.id = car.owner_id
                    LEFT JOIN car c on au.id = car.current_rented_id
                    WHERE car.car_state = 'FREE'
                    ORDER BY sqrt(pow(car.latitude - :latitude, 2) + pow(car.longitude - :longitude, 2))
                    LIMIT 10
                    """,
            nativeQuery = true
    )
    List<Car> findClosest(Double latitude, Double longitude);
}
