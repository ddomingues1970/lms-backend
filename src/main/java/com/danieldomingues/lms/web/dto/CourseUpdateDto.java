package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// Se você NÃO quiser permitir renomear o curso, mantenha sem 'name' aqui.
public record CourseUpdateDto(
        String description,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
