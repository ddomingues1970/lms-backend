package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.Student;
import com.danieldomingues.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    @Transactional
    public Student register(Student s) {
        if (repo.existsByEmail(s.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (age(s.getBirthDate()) < 16) {
            throw new IllegalArgumentException("Student must be at least 16 years old");
        }
        return repo.save(s);
    }

    private int age(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public List<Student> findAll() {
        return this.repo.findAll();
    }

}
