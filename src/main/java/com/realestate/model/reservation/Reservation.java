package com.realestate.model.reservation;

import com.realestate.model.offer.Offer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @OneToOne(optional = true)
    @JoinColumn(name = "offer_id")
    private Offer offer;
}
