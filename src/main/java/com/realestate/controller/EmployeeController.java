package com.realestate.controller;

import com.realestate.dto.ApartmentPropertyDto;
import com.realestate.dto.EmployeeDto;
import com.realestate.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin("*")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;



    @GetMapping("")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<EmployeeDto> allEmployees = employeeService.getAllEmployees(page, size);
        if (allEmployees.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allEmployees);
    }
}
