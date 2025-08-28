package com.danieldomingues.lms.web.dto;

import java.time.LocalDateTime;


public record TaskLogResponseDto(
        Long id,
        LocalDateTime dateTime,
        String description,
        Integer minutesSpent,
        String category,
        String course,
        String student
) {}