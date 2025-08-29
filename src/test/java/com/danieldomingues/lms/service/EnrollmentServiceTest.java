package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Course;
import com.danieldomingues.lms.domain.Enrollment;
import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.repository.CourseRepository;
import com.danieldomingues.lms.repository.EnrollmentRepository;
import com.danieldomingues.lms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepo;
    @Mock
    private StudentRepository studentRepo;
    @Mock
    private CourseRepository courseRepo;

    @InjectMocks
    private EnrollmentService service;

    private Student student;
    private Course course;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Instâncias concretas (seguindo seu padrão: no-args + setters)
        student = new Student();
        student.setFirstName("Ana");
        student.setLastName("Silva");
        student.setBirthDate(LocalDate.now().minusYears(20));
        student.setEmail("ana@example.com");
        student.setPhone("11999999999");

        course = new Course();
        course.setName("Java Básico");
        course.setDescription("Intro");
        course.setStartDate(LocalDate.now());
        course.setEndDate(LocalDate.now().plusMonths(3));
    }

    @Test
    @DisplayName("Deve matricular quando estudante/curso existem, não duplicado e < 3 cursos")
    void enroll_ok() {
        Long studentId = 1L, courseId = 10L;

        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(false);
        when(enrollmentRepo.findByStudent(student)).thenReturn(List.of( // já tem 2, pode matricular o 3º
                new Enrollment(student, new Course()),
                new Enrollment(student, new Course())
        ));
        // retorna a própria matrícula salva
        when(enrollmentRepo.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

        Enrollment e = service.enroll(studentId, courseId);

        assertNotNull(e);
        assertEquals(student, e.getStudent());
        assertEquals(course, e.getCourse());
        verify(enrollmentRepo).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Deve falhar se estudante não existir")
    void enroll_student_not_found() {
        Long studentId = 1L, courseId = 10L;

        when(studentRepo.findById(studentId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.enroll(studentId, courseId));
        assertTrue(ex.getMessage().toLowerCase().contains("student"));
        verify(courseRepo, never()).findById(anyLong());
        verify(enrollmentRepo, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar se curso não existir")
    void enroll_course_not_found() {
        Long studentId = 1L, courseId = 10L;

        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.enroll(studentId, courseId));
        assertTrue(ex.getMessage().toLowerCase().contains("course"));
        verify(enrollmentRepo, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar se matrícula já existir (duplicada)")
    void enroll_duplicate_should_fail() {
        Long studentId = 1L, courseId = 10L;

        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.enroll(studentId, courseId));
        assertTrue(ex.getMessage().toLowerCase().contains("already"));
        verify(enrollmentRepo, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao tentar 4ª matrícula (limite de 3)")
    void enroll_limit_exceeded_should_fail() {
        Long studentId = 1L, courseId = 10L;

        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepo.existsByStudentIdAndCourseId(studentId, courseId)).thenReturn(false);
        when(enrollmentRepo.findByStudent(student)).thenReturn(List.of(
                new Enrollment(student, new Course()),
                new Enrollment(student, new Course()),
                new Enrollment(student, new Course())
        ));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.enroll(studentId, courseId));
        assertTrue(ex.getMessage().toLowerCase().contains("3"));
        verify(enrollmentRepo, never()).save(any());
    }

    @Test
    @DisplayName("listByStudent retorna matrículas quando estudante existe")
    void listByStudent_ok() {
        Long studentId = 1L;
        when(studentRepo.findById(studentId)).thenReturn(Optional.of(student));
        when(enrollmentRepo.findByStudent(student)).thenReturn(List.of(
                new Enrollment(student, course)
        ));

        List<Enrollment> list = service.listByStudent(studentId);

        assertEquals(1, list.size());
        assertEquals(student, list.get(0).getStudent());
        verify(enrollmentRepo).findByStudent(student);
    }

    @Test
    @DisplayName("listByStudent deve falhar se estudante não existir")
    void listByStudent_student_not_found() {
        Long studentId = 1L;
        when(studentRepo.findById(studentId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.listByStudent(studentId));
        assertTrue(ex.getMessage().toLowerCase().contains("student"));
        verify(enrollmentRepo, never()).findByStudent(any());
    }
}
