package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "courses", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    // getters/setters
}
