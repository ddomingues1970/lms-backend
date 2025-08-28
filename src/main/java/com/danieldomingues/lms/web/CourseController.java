package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody Course c) {
        return ResponseEntity.ok(service.create(c));
    }
}
