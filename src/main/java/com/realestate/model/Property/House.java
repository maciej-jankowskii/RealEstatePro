package com.realestate.model.Property;

import com.realestate.enums.BuildingType;
import com.realestate.enums.Standard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "house")
public class House  extends Property{
    private Double landArea;
    private Double houseArea;
    private Integer rooms;
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
