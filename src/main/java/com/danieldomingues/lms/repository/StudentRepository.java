package com.danieldomingues.lms.repository;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
}
