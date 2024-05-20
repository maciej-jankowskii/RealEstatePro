package com.realestate.service;

import com.realestate.config.JWTGenerator;
import com.realestate.dto.LoginDto;
import com.realestate.dto.RegisterDto;
import com.realestate.exceptions.RegistrationException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.exceptions.UnauthorizedException;
import com.realestate.model.user.Role;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.RoleRepository;
import com.realestate.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final ValidationService validator;


    public void registerUser(@Valid RegisterDto registerDto) {
        validator.validateData(registerDto);
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RegistrationException("Email is taken");
        }
        UserEmployee user = createEmployee(registerDto);

        userRepository.save(user);
    }



    public String loginUser(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return jwtGenerator.generatedToken(authenticate);
        } catch (AuthenticationException exception){
            throw new UnauthorizedException("Invalid data");
        }
    }

    /**

        HELPER METHODS FOR REGISTER

     **/


    private UserEmployee createEmployee(RegisterDto registerDto) {
        UserEmployee user = new UserEmployee();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roleAgent = roleRepository.findByRoleName("AGENT").orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRoles(Collections.singletonList(roleAgent));
        return user;
    }
}
