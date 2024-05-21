package com.realestate.model.Property;

import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "apartment")
public class Apartment extends Property {

    private Double area;
    private Integer rooms;
    private Integer bathrooms;
    private Boolean duplexApartment;
    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;
    private Integer floor;
    private Boolean elevator;
    private Boolean balcony;
    private Boolean garage;
    private Integer yearOfConstruction;
    @Enumerated(EnumType.STRING)
    private Standard standard;
}
