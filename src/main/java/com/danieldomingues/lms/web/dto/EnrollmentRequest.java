package com.danieldomingues.lms.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollmentRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;
}
