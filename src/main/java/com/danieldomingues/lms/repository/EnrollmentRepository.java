package com.danieldomingues.lms.repository;

import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import com.danieldomingues.lms.domain.EnrollmentStatus;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    long countByStudentIdAndStatus(Long studentId, EnrollmentStatus status);
}

