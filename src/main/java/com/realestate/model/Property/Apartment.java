package com.realestate.model.Property;

import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "apartment")
public class Apartment extends Property {
    @NotNull
    @Min(1)
    private Double area;
    @NotNull
    private Integer rooms;
    @NotNull
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
