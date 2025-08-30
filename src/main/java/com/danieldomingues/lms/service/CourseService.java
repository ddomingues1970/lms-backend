package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.mapper.CourseMapper;
import com.danieldomingues.lms.repository.CourseRepository;
import com.danieldomingues.lms.web.dto.CourseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public Course update(Long id, Course data) {
        Course existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found"));

        if (!existing.getName().equals(data.getName()) && repo.existsByName(data.getName())) {
            throw new IllegalArgumentException("Course name must be unique");
        }
        if (data.getEndDate().isAfter(data.getStartDate().plusMonths(6))) {
            throw new IllegalArgumentException("Course must finish within 6 months from start date");
        }

        existing.update(data.getDescription(), data.getStartDate(), data.getEndDate());
        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Course not found");
        }
        repo.deleteById(id);
    }

    public List<Course> findAll() {
        return this.repo.findAll();
    }

    public CourseDto getById(Long id) {
        Course entity = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));
        return CourseMapper.toDTO(entity); // adapte para seu mapper/DTO
    }
}
