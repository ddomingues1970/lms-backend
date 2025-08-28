package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CourseCreateDto(
        @NotBlank String name,
        String description,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
