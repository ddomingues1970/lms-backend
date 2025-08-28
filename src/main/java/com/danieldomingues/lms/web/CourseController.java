package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.service.CourseService;
import com.danieldomingues.lms.web.dto.CourseCreateDto;
import com.danieldomingues.lms.web.dto.CourseUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;
    public CourseController(CourseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody CourseCreateDto dto) {
        Course c = new Course();
        c.setName(dto.name());
        c.setDescription(dto.description());
        c.setStartDate(dto.startDate());
        c.setEndDate(dto.endDate());
        return ResponseEntity.ok(service.create(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @Valid @RequestBody CourseUpdateDto dto) {
        // monta um "conteúdo" para reaproveitar a lógica atual do service
        Course data = new Course();
        data.setDescription(dto.description());
        data.setStartDate(dto.startDate());
        data.setEndDate(dto.endDate());
        return ResponseEntity.ok(service.update(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Course>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }
}