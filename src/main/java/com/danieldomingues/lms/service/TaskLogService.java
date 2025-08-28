package com.danieldomingues.lms.service;

import com.danieldomingues.lms.domain.*;
import com.danieldomingues.lms.repository.*;
import com.danieldomingues.lms.web.dto.TaskLogCreateDto;
import com.danieldomingues.lms.web.dto.TaskLogUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskLogService {

    private final TaskLogRepository repo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;
    private final TaskCategoryRepository categoryRepo;

    public TaskLogService(TaskLogRepository repo, StudentRepository studentRepo,
                          CourseRepository courseRepo, TaskCategoryRepository categoryRepo) {
        this.repo = repo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<TaskLog> listByStudent(Long studentId) {
        return repo.findByStudentId(studentId);
    }

    @Transactional
    public TaskLog create(TaskLogCreateDto dto) {
        Student student = studentRepo.findById(dto.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepo.findById(dto.courseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        TaskCategory category = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (dto.minutesSpent() % 30 != 0) {
            throw new IllegalArgumentException("Time must be a multiple of 30 minutes");
        }

        return repo.save(new TaskLog(student, course, category, dto.timestamp(), dto.description(), dto.minutesSpent()));
    }

    @Transactional
    public TaskLog update(Long id, TaskLogUpdateDto dto) {
        TaskLog log = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TaskLog not found"));

        TaskCategory category = categoryRepo.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (dto.minutesSpent() % 30 != 0) {
            throw new IllegalArgumentException("Time must be a multiple of 30 minutes");
        }

        log.update(category, dto.timestamp(), dto.description(), dto.minutesSpent());
        return log;
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("TaskLog not found");
        }
        repo.deleteById(id);
    }
}
