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

@Getter
@Setter
@Entity
@Table(name = "house")
public class House extends Property {
    @NotNull
    @Min(1)
    private Double landArea;
    @NotNull
    @Min(1)
    private Double houseArea;
    @NotNull
    private Integer rooms;
    @NotNull
    private Integer bathrooms;
    private Boolean balcony;
    private Boolean garage;
    private Boolean twoStoryHouse;
    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;
    private Integer yearOfConstruction;
    @Enumerated(EnumType.STRING)
    private Standard standard;
}
