package ru.valkov.carsharing.carsharingapplication.car;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.valkov.carsharing.carsharingapplication.user.AppUser;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "car_sequence_id"
    )
    @SequenceGenerator(
            name = "car_sequence_id",
            sequenceName = "car_sequence_id",
            allocationSize = 1
    )
    private Long id;
    private Double longitude;
    private Double latitude;
    private String name;
    @Enumerated(EnumType.STRING)
    private CarState carState;
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "owner_id", referencedColumnName = "id"
    )
    @JsonBackReference(
            value = "carsReference"
    )
    private AppUser owner;
    @OneToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "current_rented_id", referencedColumnName = "id"
    )
    @JsonBackReference(
            value = "rentedCar"
    )
    private AppUser currentRenter;
    private BigDecimal pricePerMinute;
    private String carNumber;
    private LocalDateTime rentedFrom;
}
