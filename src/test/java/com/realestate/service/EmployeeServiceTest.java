package com.realestate.service;

import com.realestate.dto.EmployeeDto;
import com.realestate.mapper.EmployeeMapper;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        int page = 0;
        int size = 10;
        List<UserEmployee> employeeList = new ArrayList<>();
        employeeList.add(new UserEmployee());
        employeeList.add(new UserEmployee());
        Page<UserEmployee> employeePage = mock(Page.class);
        when(employeePage.getContent()).thenReturn(employeeList);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(employeePage);

        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        employeeDtoList.add(new EmployeeDto());
        employeeDtoList.add(new EmployeeDto());
        when(employeeMapper.map(any(UserEmployee.class))).thenReturn(new EmployeeDto());

        List<EmployeeDto> result = employeeService.getAllEmployees(page, size);

        assertEquals(employeeDtoList.size(), result.size());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
    }
}
