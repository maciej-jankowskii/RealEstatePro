package com.realestate.service;

import com.realestate.config.JWTGenerator;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.exceptions.RegistrationException;
import com.realestate.exceptions.UnauthorizedException;
import com.realestate.model.user.Role;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.RoleRepository;
import com.realestate.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.Set;

@Service
@Validated
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final Validator validator;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTGenerator jwtGenerator, Validator validator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
        this.validator = validator;
    }

    public String registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return "Email is taken!";
        }

        UserEmployee user = new UserEmployee();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<ConstraintViolation<UserEmployee>> violationSet = validator.validate(user);
        if (!violationSet.isEmpty()){
            throw new RegistrationException("Invalid user data");
        }

        Role roleAgent = roleRepository.findByRoleName("AGENT").orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Collections.singletonList(roleAgent));

        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtGenerator.generatedToken(authenticate);
            return token;
        } catch (AuthenticationException exception){
            throw new UnauthorizedException("Invalid data");
        }
    }
}
