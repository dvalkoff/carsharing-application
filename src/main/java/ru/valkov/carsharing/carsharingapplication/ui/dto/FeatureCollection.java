package ru.valkov.carsharing.carsharingapplication.ui.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FeatureCollection implements Serializable {
    private String type;
    private List<Feature> features;
}
