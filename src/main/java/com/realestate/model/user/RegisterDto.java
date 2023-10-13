package com.realestate.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
