package com.realestate.dto;

import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.reservation.Reservation;
import com.realestate.model.user.UserEmployee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfferDto {
    private Long id;
    private Long userId;
    private Long clientId;
    private Long propertyId;
    private Long reservationId;
}
