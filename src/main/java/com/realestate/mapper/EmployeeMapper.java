package com.realestate.mapper;

import com.realestate.dto.EmployeeDto;
import com.realestate.model.user.UserEmployee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {

    public EmployeeDto map(UserEmployee employee){
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        return dto;
    }
}
