package com.realestate.controller;

import com.realestate.config.JWTGenerator;
import com.realestate.dto.AuthResponseDto;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.model.user.*;
import com.realestate.repository.RoleRepository;
import com.realestate.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        if (userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEmployee user = new UserEmployee();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roleAgent = roleRepository.findByName("AGENT").get();
        user.setRoles(Collections.singletonList(roleAgent));

        userRepository.save(user);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtGenerator.generatedToken(authenticate);
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
        } catch (BadCredentialsException ex){
            AuthResponseDto responseDto = new AuthResponseDto(null);
            responseDto.setErrorMessage("Invalid e-mail or password");
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

    }




}
