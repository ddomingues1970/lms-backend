package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.repository.CourseRepository;
import com.danieldomingues.lms.repository.EnrollmentRepository;
import com.danieldomingues.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo,
                             StudentRepository studentRepo,
                             CourseRepository courseRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        List<Enrollment> currentEnrollments = enrollmentRepo.findByStudent(student);
        if (currentEnrollments.size() >= 3) {
            throw new IllegalArgumentException("Student cannot be enrolled in more than 3 courses");
        }

        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepo.save(enrollment);
    }

    public List<Enrollment> listByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return enrollmentRepo.findByStudent(student);
    }
}
