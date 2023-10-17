package com.realestate.controller;

import com.realestate.dto.AuthResponseDto;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        String result = authService.registerUser(registerDto);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        String token = authService.loginUser(loginDto);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);


    }
}
