package com.example.Testing.application.Service;

import com.example.Testing.application.Entitys.Employee;
import com.example.Testing.application.Exception.ResourceNotFoundException;
import com.example.Testing.application.dto.EmployeeDto;
import com.example.Testing.application.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
@Slf4j
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;

    @BeforeEach
    void setUp(){
        mockEmployee=Employee.builder()
                .id(1L)
                .name("Bittu Bag")
                .email("bagbittu10@gmail.com")
                .phoneNumber("1236780381")
                .build();

        mockEmployeeDto =modelMapper.map(mockEmployee,EmployeeDto.class);
    }


    @Test
    void testEmployeeById_whenEmployeeIdIsPresent_thenReturnEmployeeDto(){

        // assign

        Long id=mockEmployee.getId();

        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        //act

        EmployeeDto employeeDto=employeeService.getEmployeeById(id);

        //assert

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository,only()).findById(id);

    }

    @Test
    void testGateEmployeeById_whenEmployeeIsNotPresent_thenThrowException(){
        //assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act &&  assert

        assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id 1");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_whenValidEmployee_thenCreateNewEmployee(){
        //assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);

        //act

        EmployeeDto employeeDto=employeeService.addEmployee(mockEmployeeDto);

        //assert


        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(employeeDto.getEmail());

        ArgumentCaptor<Employee>employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee captureEmployee= employeeArgumentCaptor.getValue();
        assertThat(captureEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());



    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException(){
        //assign
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));


        //act and assert

        assertThatThrownBy(()->employeeService.addEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exist with email: "+mockEmployee.getEmail());
        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());


    }
    @Test
    void testUpdatePartialEmployee_whenEmployeeExists_thenUpdateOnlyProvidedFields() {
        // Arrange
        Long id = 1L;
        Map<String, Object> updates = Map.of("name", "New Bittu Name");


        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));


        Employee updatedEmployee = mockEmployee;
        updatedEmployee.setName("New Bittu Name");
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        EmployeeDto result = employeeService.updatePartialEmployeeById(id, updates);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Bittu Name");

        // Verify specifically the saving part
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository, atLeastOnce()).save(employeeCaptor.capture());
        assertThat(employeeCaptor.getValue().getName()).isEqualTo("New Bittu Name");
    }
    @Test
    void testUpdatePartialEmployee_whenEmployeeDoesNotExist_thenThrowException() {
        // Arrange
        Long id = 5L;
        Map<String, Object> updates = Map.of("name", "New Name");

        // Mocking findById to return Empty Optional
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updatePartialEmployeeById(id, updates))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id " + id);

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void testDeleteEmployee_whenEmployeeIdIsValid_thenDeleteEmployee() {
        // Arrange
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(id);

        // Act
        employeeService.deleteEmployee(id);

        // Assert
        verify(employeeRepository, times(1)).existsById(id);
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEmployee_whenEmployeeIdIsNotValid_thenThrowException() {
        // Arrange
        Long id = 1L;
        when(employeeRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id " + id);

        verify(employeeRepository, never()).deleteById(anyLong());
    }

}