package com.realestate.model.client;

import com.realestate.model.offer.Offer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 2)
    private String firstName;
    @NotNull
    @Size(min = 2)
    private String lastName;
    @NotNull
    @Size(min = 9)
    private String telephone;
    @NotNull
    @Size(min = 5)
    @Email
    private String email;
    @OneToMany(mappedBy = "client")
    private List<Offer> offers = new ArrayList<>();
}
