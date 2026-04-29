package com.example.Testing.application.Service;

import com.example.Testing.application.dto.EmployeeDto;

import java.util.Map;


public interface EmployeeService {
    EmployeeDto addEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long id);

    void deleteEmployee(Long id);

    EmployeeDto updatePartialEmployeeById(Long employeeId, Map<String, Object> update);
}
