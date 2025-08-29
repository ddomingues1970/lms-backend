package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.service.EnrollmentService;
import com.danieldomingues.lms.web.dto.EnrollmentRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    /** Matricula um estudante no curso (201 Created).
     *  Regras são aplicadas no service:
     *  - 404 se student/curso não existem
     *  - 409 se já matriculado no mesmo curso
     *  - 422 se já possui 3 matrículas ativas
     */
    @PostMapping
    public ResponseEntity<Enrollment> enroll(@Valid @RequestBody EnrollmentRequestDto dto) {
        Enrollment saved = service.enroll(dto.studentId(), dto.courseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Lista todas as matrículas de um estudante (qualquer status). */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> listByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.listByStudent(studentId));
    }
}
