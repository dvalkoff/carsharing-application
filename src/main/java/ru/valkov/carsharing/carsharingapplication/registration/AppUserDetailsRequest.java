package ru.valkov.carsharing.carsharingapplication.registration;

import lombok.Data;

@Data
public class AppUserDetailsRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}
