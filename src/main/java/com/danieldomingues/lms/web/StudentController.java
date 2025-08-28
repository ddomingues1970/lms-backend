package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.service.StudentService;
import com.danieldomingues.lms.web.dto.StudentCreateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Student> register(@Valid @RequestBody StudentCreateDto dto) {
        Student s = new Student();
        s.setFirstName(dto.firstName());
        s.setLastName(dto.lastName());
        s.setEmail(dto.email());
        s.setBirthDate(dto.birthDate());
        s.setPhone(dto.phone());
        return ResponseEntity.ok(service.register(s));
    }

    @GetMapping
    public ResponseEntity<java.util.List<Student>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
