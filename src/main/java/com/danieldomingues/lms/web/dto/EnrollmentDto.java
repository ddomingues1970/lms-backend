package com.danieldomingues.lms.web.dto;

import com.danieldomingues.lms.domain.EnrollmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EnrollmentDto {
    private Long id;
    private Long studentId;
    private Long courseId;
    private EnrollmentStatus status;
    private LocalDate enrollmentDate;
}
