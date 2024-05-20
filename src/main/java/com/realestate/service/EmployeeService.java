package com.realestate.service;

import com.realestate.dto.EmployeeDto;
import com.realestate.dto.OfferDto;
import com.realestate.mapper.EmployeeMapper;
import com.realestate.model.offer.Offer;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeDto> getAllEmployees(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEmployee> employees = userRepository.findAll(pageable);
        return employees.getContent().stream().map(employeeMapper::map).collect(Collectors.toList());

    }
}
