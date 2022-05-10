package ru.valkov.carsharing.carsharingapplication.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.valkov.carsharing.carsharingapplication.car.Car;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @JsonManagedReference(
            value = "carsReference"
    )
    private Set<Car> cars;
    @OneToOne(
            mappedBy = "currentRenter"
    )
    @JsonManagedReference(
            value = "rentedCar"
    )
    private Car rentedCar;
}
