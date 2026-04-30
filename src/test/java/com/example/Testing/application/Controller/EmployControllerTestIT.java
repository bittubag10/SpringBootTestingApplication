package com.example.Testing.application.Controller;

import com.example.Testing.application.Entitys.Employee;
import com.example.Testing.application.dto.EmployeeDto;
import com.example.Testing.application.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
class EmployControllerTestIT {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee  testEmployee;

    private EmployeeDto testEmployeeDto;


    @BeforeEach
    void setUp(){
        testEmployee= Employee.builder()
                .id(1L)
                .name("Bittu Bag")
                .email("bagbittu10@gmail.com")
                .phoneNumber("1274382663")
                .build();
        testEmployeeDto=EmployeeDto.builder()
                .id(1L)
                .name("Bittu Bag")
                .email("bagbittu10@gmail.com")
                .phoneNumber("1274382663")
                .build();

    }
    @BeforeEach
    void cleanUp() {
        employeeRepository.deleteAll();

    }

    @Test
    void Test() {

        testEmployee.setId(null); // Ensure ID is null so DB generates it
        Employee savedEmployee = employeeRepository.save(testEmployee);


        webTestClient.get()
                .uri("/employee/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.name").isEqualTo(testEmployee.getName())
                .jsonPath("$.email").isEqualTo(testEmployee.getEmail());
    }

    @Test
    void TestEmployeeById_failure(){
        webTestClient.get()
                .uri("/employee/{id}",999L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_success() {
        // Arrange
        EmployeeDto inputDto = EmployeeDto.builder()
                .name("Arjun")
                .email("arjun@gmail.com")
                .phoneNumber("9876543210")
                .build();

        // Act & Assert
        webTestClient.post()
                .uri("/employee/add")
                .bodyValue(inputDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Arjun")
                .jsonPath("$.email").isEqualTo("arjun@gmail.com");


        assertThat(employeeRepository.findByEmail("arjun@gmail.com")).isNotEmpty();
    }

    @Test
    void testUpdatePartialEmployee_success() {
        // Arrange
        Employee savedEmployee = employeeRepository.save(testEmployee);
        Map<String, Object> updates = Map.of("name", "Updated Name");

        // Act & Assert
        webTestClient.patch()
                .uri("/employee/{id}", savedEmployee.getId())
                .bodyValue(updates)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Updated Name")
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
    }
    @Test
    void testUpdatePartialEmployee_failure_whenIdNotFound() {
        // Arrange
        Map<String, Object> updates = Map.of("name", "New Name");

        // Act & Assert
        webTestClient.patch()
                .uri("/employee/{id}", 999L)
                .bodyValue(updates)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_success() {
        // Arrange
        Employee savedEmployee = employeeRepository.save(testEmployee);

        // Act & Assert
        webTestClient.delete()
                .uri("/employee/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent();


        assertThat(employeeRepository.existsById(savedEmployee.getId())).isFalse();
    }

    @Test
    void testDeleteEmployee_failure() {

        webTestClient.delete()
                .uri("/employee/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound();
    }



}