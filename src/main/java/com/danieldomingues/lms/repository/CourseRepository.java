package com.danieldomingues.lms.repository;

import com.danieldomingues.lms.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByName(String name);
}
