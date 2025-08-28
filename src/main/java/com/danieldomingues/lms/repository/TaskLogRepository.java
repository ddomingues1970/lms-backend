package com.danieldomingues.lms.repository;

import com.danieldomingues.lms.domain.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    List<TaskLog> findByStudentId(Long studentId);
    List<TaskLog> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
