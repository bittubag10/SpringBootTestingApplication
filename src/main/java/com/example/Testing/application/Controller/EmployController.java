package com.example.Testing.application.Controller;

import com.example.Testing.application.Service.EmployeeService;
import com.example.Testing.application.dto.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto){
        EmployeeDto employee=employeeService.addEmployee(employeeDto);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id){
        EmployeeDto employee=employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDto> updatePartialEmployeeById(@RequestBody Map<String, Object> update,
                                                                 @PathVariable Long employeeId) {
        EmployeeDto employeeDTO = employeeService.updatePartialEmployeeById(employeeId, update);

        if (employeeDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employeeDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
