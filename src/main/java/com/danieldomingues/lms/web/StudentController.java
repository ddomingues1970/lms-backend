package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Student> register(@RequestBody Student s) {
        return ResponseEntity.ok(service.register(s));
    }
}
