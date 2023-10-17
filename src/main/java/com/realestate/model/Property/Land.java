package com.realestate.model.Property;

import com.realestate.enums.TypeOfLand;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "land")
public class Land extends Property{
    @Enumerated(EnumType.STRING)
    private TypeOfLand typeOfLand;
    private Double area;
    private Boolean buildingPermit;

}
