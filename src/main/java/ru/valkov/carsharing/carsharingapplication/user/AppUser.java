package ru.valkov.carsharing.carsharingapplication.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.valkov.carsharing.carsharingapplication.car.Car;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {
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
    @Column(nullable = false, unique = true)
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

    @Enumerated(EnumType.STRING)
    private AppUserRole userRole;

    @Column(nullable = false)
    private boolean isAccountNonExpired;

    @Column(nullable = false)
    private boolean isAccountNonLocked;

    @Column(nullable = false)
    private boolean isCredentialsNonExpired;

    @Column(nullable = false)
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
