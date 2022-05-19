package ru.valkov.carsharing.carsharingapplication.ui.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarRequest {
    private Double latitude;
    private Double longitude;
    private String name;
    private String carNumber;
    private BigDecimal pricePerMinute;
}
