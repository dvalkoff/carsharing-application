package ru.valkov.carsharing.carsharingapplication.ui.dto;

import lombok.Data;

@Data
public class Feature {
    private String type;
    private Long id;
    private Geometry geometry;
    private Properties properties;
}
