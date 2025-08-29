package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate enrollmentDate = LocalDate.now();

    @Enumerated(EnumType.STRING)                 // <-- novo
    @Column(name = "status", nullable = false, length = 16)
    @Setter                                       // <-- permitir mudança do status
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }
}
