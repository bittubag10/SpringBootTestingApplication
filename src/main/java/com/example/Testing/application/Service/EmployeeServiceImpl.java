package com.example.Testing.application.Service;

import com.example.Testing.application.Entitys.Employee;
import com.example.Testing.application.Exception.ResourceNotFoundException;
import com.example.Testing.application.dto.EmployeeDto;
import com.example.Testing.application.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService{
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto addEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee with email : {}",employeeDto.getEmail());

        List<Employee> existingEmployee=employeeRepository.findByEmail(employeeDto.getEmail());

        if (!existingEmployee.isEmpty()){
            log.error("Employee already exist with email: {} ",employeeDto.getEmail());
            throw new RuntimeException("Employee already exist with email: "+employeeDto.getEmail());
        }

        Employee employee=modelMapper.map(employeeDto,Employee.class);
        Employee savedEmployee=employeeRepository.save(employee);

        log.info("Successfully created new employee with id: {}",employeeDto.getId());
        return modelMapper.map(savedEmployee,EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        log.info("fetching employee with id : {}",id);
       Employee employee=employeeRepository.findById(id).orElseThrow(()->{
           log.error("Employee not found with id : {} " ,id);
           return new ResourceNotFoundException("Employee not found with id "+id);
       });

       return modelMapper.map(employee,EmployeeDto.class);

    }

    public EmployeeDto updatePartialEmployeeById(Long employeeId, Map<String, Object> update) {

        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->{
            log.error("Employee not found with employeeId : {} " , employeeId);
            return new ResourceNotFoundException("Employee not found with id "+employeeId);
        });

        Employee employeeEntity = employeeRepository.findById(employeeId).get();


        update.forEach((field, value) -> {

            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(Employee.class, field);

            fieldToBeUpdated.setAccessible(true);

            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });

        Employee savedEmployee = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }


    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        if (!employeeRepository.existsById(id)){
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }
        employeeRepository.deleteById(id);
    }


}
