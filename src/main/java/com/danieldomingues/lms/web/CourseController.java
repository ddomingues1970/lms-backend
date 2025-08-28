package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody Course c) {
        return ResponseEntity.ok(service.create(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course c) {
        return ResponseEntity.ok(service.update(id, c));
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
