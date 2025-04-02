package com.lmar.seek_customer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long id;
    private String names;
    private String lastName;
    private int age;
    private LocalDate birthday;
    private LocalDate estimatedDeathDate;
    private String zodiacSign;

    public CustomerDto(Long id, String names, String lastName, int age, LocalDate birthday) {
        this.id = id;
        this.names = names;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
    }

    public CustomerDto(String names, String lastName, int age, LocalDate birthday) {
        this.names = names;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
    }
}
