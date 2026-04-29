package com.example.Testing.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

}
