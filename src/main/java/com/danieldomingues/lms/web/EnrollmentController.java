package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Enrollment> enroll(@RequestParam Long studentId, @RequestParam Long courseId) {
        return ResponseEntity.ok(service.enroll(studentId, courseId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> listByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.listByStudent(studentId));
    }
}
