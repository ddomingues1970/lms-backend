package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record TaskLogCreateDto(
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull Long categoryId,
        @NotNull LocalDateTime timestamp,
        @NotBlank String description,
        @Min(30) Integer minutesSpent
) {}
