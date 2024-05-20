package com.realestate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    @NotBlank(message = "Cannot be empty")
    @Size(min = 2, message = "The name must have at least 2 characters ")
    private String firstName;
    @NotBlank(message = "Cannot be empty")
    @Size(min = 2, message = "The surname must have at least 2 characters ")
    private String lastName;
    @NotBlank(message = "Cannot be empty")
    @Email(message = "Incorrect e-mail address format")
    private String email;
    @NotBlank(message = "Cannot be empty")
    @Size(min = 4,message = "Password must have at least 4 characters")
    private String password;

}
