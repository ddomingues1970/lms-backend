package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "students", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotBlank @Email
    private String email;

    private String phone;

    // getters/setters
}
