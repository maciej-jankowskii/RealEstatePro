package com.realestate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {

    private String accessToken;
    private String tokenType = "Bearer ";
    private String errorMessage;

    public AuthResponseDto(String accessToken){
        this.accessToken = accessToken;
    }

}
