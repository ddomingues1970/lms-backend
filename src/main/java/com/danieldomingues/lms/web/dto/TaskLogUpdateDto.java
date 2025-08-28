package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


public record TaskLogUpdateDto(
        @NotNull Long categoryId,
        @NotNull LocalDateTime timestamp,
        @NotBlank String description,
        @Min(30) Integer minutesSpent
) {

    public LocalDateTime timestamp() {
        return LocalDateTime.now();
    }
}
