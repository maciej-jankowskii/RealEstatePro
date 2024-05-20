package com.realestate.controller;

import com.realestate.dto.AuthResponseDto;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto) {
        authService.registerUser(registerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        String token = authService.loginUser(loginDto);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);


    }
}
