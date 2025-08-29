package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.domain.EnrollmentStatus;
import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.repository.CourseRepository;
import com.danieldomingues.lms.repository.EnrollmentRepository;
import com.danieldomingues.lms.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * Matricula um estudante em um curso aplicando as regras:
     * - NÃO duplicar matrícula no mesmo curso (409)
     * - Máximo de 3 matrículas ATIVAS por estudante (422)
     * - 404 caso student ou course não existam
     */
    @Transactional
    public Enrollment enroll(Long studentId, Long courseId) {
        // 1) valida existência
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado"));
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        // 2) bloqueia duplicidade (mesmo aluno + mesmo curso)
        if (enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estudante já matriculado neste curso");
        }

        // 3) regra ≤ 3 matrículas ATIVAS
        long activeCount = enrollmentRepo.countByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE);
        if (activeCount >= 3) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Estudante já possui 3 matrículas ativas");
        }

        // 4) cria matrícula (status default = ACTIVE na entidade)
        Enrollment enrollment = new Enrollment(student, course);
        return enrollmentRepo.save(enrollment);
    }

    /** Lista todas as matrículas do estudante (qualquer status). */
    public List<Enrollment> listByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado"));
        return enrollmentRepo.findByStudent(student);
    }
}
