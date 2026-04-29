package com.example.Testing.application.repository;

import com.example.Testing.application.Entitys.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        employee= Employee.builder()
                .id(1L)
                .name("Bittu Bag")
                .email("bittu10@gmail.com")
                .phoneNumber("1234543278")
                .build();

    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee(){
        //Arrange, Given

        employeeRepository.save(employee);

        //Act,When

        List<Employee>employeeList = employeeRepository.findByEmail(employee.getEmail());

        //Assert, Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }
    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmployee(){

        //given

        String email="notPresent.124@gmail.com";

        //when
        List<Employee>employeeList=employeeRepository.findByEmail(email);

        //then

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
    }

}