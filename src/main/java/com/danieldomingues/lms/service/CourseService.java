package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) { this.repo = repo; }

    @Transactional
    public Course create(Course c) {
        if (repo.existsByName(c.getName())) {
            throw new IllegalArgumentException("Course name must be unique");
        }
        if (c.getEndDate().isAfter(c.getStartDate().plusMonths(6))) {
            throw new IllegalArgumentException("Course must finish within 6 months from start date");
        }
        return repo.save(c);
    }
}
