package com.realestate.controller;

import com.realestate.config.JWTGenerator;
import com.realestate.dto.AuthResponseDto;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.model.user.*;
import com.realestate.repository.RoleRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto){
        String result = authService.registerUser(registerDto);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        String token = authService.loginUser(loginDto);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);


    }
}
