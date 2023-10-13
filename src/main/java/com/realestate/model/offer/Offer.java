package com.realestate.model.offer;

import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.reservation.Reservation;
import com.realestate.model.user.UserEmployee;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEmployee user;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    private Property property;
    @OneToOne
    private Reservation reservation;
}
