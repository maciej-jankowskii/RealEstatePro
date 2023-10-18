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
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTGenerator jwtGenerator;
    @Mock private Validator validator;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerTest() {
        RegisterDto dto = new RegisterDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        Role role = new Role();
        role.setRoleName("AGENT");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName("AGENT")).thenReturn(Optional.of(role));

        UserEmployee user = new UserEmployee();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        when(userRepository.save(Mockito.any(UserEmployee.class))).thenReturn(user);

        String result = authService.registerUser(dto);

        assertEquals("User registered successfully!", result);

    }

    @Test
    public void testRegisterWithTakenEmail() {
        RegisterDto dto = new RegisterDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        String result = authService.registerUser(dto);
        assertEquals("Email is taken!", result);
        verify(userRepository, never()).save(any(UserEmployee.class));

    }

    @Test
    public void testRegisterWithInvalidData() {
        RegisterDto dto = new RegisterDto();
        dto.setFirstName("");
        dto.setLastName("Doe");
        dto.setEmail("wrong email");
        dto.setPassword("p");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        Role role = new Role();
        role.setRoleName("AGENT");
        when(roleRepository.findByRoleName(role.getRoleName())).thenReturn(Optional.of(role));

        try {
            authService.registerUser(dto);
        } catch (RegistrationException ex) {
            assertEquals("Invalid user data", ex.getMessage());
        }
    }

    @Test
    public void loginTest() {
        LoginDto dto = new LoginDto();
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        Authentication authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtGenerator.generatedToken(authentication)).thenReturn("secrettoken");

        String token = authService.loginUser(dto);

        assertEquals("secrettoken", token);
    }

    @Test
    public void testLoginWithInvalidData() {
        LoginDto dto = new LoginDto();
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new AuthenticationException("Invalid data") {
        });

        try {
            authService.loginUser(dto);
        } catch (UnauthorizedException e) {
            assertEquals("Invalid data", e.getMessage());
        }

    }
}