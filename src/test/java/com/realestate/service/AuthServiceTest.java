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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTGenerator jwtGenerator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ValidDto_ShouldRegisterUser() {

        RegisterDto registerDto = new RegisterDto();
        UserEmployee user = new UserEmployee();
        Role roleAgent = new Role();
        roleAgent.setRoleName("AGENT");
        user.setRoles(Collections.singletonList(roleAgent));

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(roleRepository.findByRoleName("AGENT")).thenReturn(Optional.of(roleAgent));
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");

        authService.registerUser(registerDto);

        verify(validationService, times(1)).validateData(registerDto);
        verify(userRepository, times(1)).existsByEmail(registerDto.getEmail());
        verify(passwordEncoder, times(1)).encode(registerDto.getPassword());
        verify(userRepository, times(1)).save(any(UserEmployee.class));
    }

    @Test
    void registerUser_ExistingEmail_ShouldThrowRegistrationException() {
        RegisterDto registerDto = new RegisterDto();

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> authService.registerUser(registerDto));
        verify(userRepository, times(1)).existsByEmail(registerDto.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void loginUser_ValidDto_ShouldReturnToken() {
        LoginDto loginDto = new LoginDto();
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtGenerator.generatedToken(authentication)).thenReturn("token");

        String token = authService.loginUser(loginDto);

        assertNotNull(token);
        assertEquals("token", token);
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtGenerator, times(1)).generatedToken(authentication);
        verifyNoMoreInteractions(authenticationManager);
        verifyNoMoreInteractions(jwtGenerator);
    }

    @Test
    void loginUser_InvalidDto_ShouldThrowUnauthorizedException() {
        LoginDto loginDto = new LoginDto();

        when(authenticationManager.authenticate(any())).thenThrow(new UnauthorizedException("Invalid data"));

        assertThrows(UnauthorizedException.class, () -> authService.loginUser(loginDto));
        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoInteractions(jwtGenerator);
    }
}
