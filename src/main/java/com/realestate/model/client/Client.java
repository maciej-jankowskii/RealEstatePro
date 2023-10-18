package com.realestate.model.client;

import com.realestate.model.offer.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Size(min = 9)
    private String telephone;
    @NotBlank
    @Email
    private String email;
    @OneToMany(mappedBy = "client")
    private List<Offer> offers = new ArrayList<>();
}
