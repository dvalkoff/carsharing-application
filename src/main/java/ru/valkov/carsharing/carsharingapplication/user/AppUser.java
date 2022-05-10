package ru.valkov.carsharing.carsharingapplication.user;

import lombok.Data;
import ru.valkov.carsharing.carsharingapplication.car.Car;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
public class AppUser {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence_id"
    )
    @SequenceGenerator(
            name = "user_sequence_id",
            sequenceName = "user_sequence_id",
            allocationSize = 1
    )
    private Long id;
    @Email
    private String email;
    private String name;
    private BigDecimal cashAccount;
    private String password;
    private String phoneNumber;
    @OneToMany(
            mappedBy = "owner"
    )
    private Set<Car> cars;
    @OneToOne(
            mappedBy = "currentRenter"
    )
    private Car rentedCar;
}
