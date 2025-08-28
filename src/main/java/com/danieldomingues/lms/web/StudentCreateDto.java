package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record StudentCreateDto(
        @NotBlank(message = "firstName is required")
        String firstName,

        @NotBlank(message = "lastName is required")
        String lastName,

        @NotNull(message = "birthDate is required")
        @Past(message = "birthDate must be in the past")
        LocalDate birthDate,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        String email,

        @NotBlank(message = "phone is required")
        String phone
) {}
