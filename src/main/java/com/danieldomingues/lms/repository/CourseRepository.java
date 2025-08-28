package com.danieldomingues.lms.repository;

import com.danieldomingues.lms.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);

    @Override
    List<Course> findAll();
}
