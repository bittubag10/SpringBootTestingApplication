package com.example.Testing.application.Service;

import com.example.Testing.application.Entitys.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJdbcTest
class EmployeeServiceImplTest {
    @Autowired
    private Employee employee;

    

}