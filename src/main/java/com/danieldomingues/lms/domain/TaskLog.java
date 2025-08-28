package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_logs")
@Getter
public class TaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private Course course;

    @ManyToOne(optional = false)
    private TaskCategory category;

    @NotNull
    private LocalDateTime timestamp;

    @NotBlank
    private String description;

    @Min(30)
    @Max(1440) // 24h em minutos, como limite de segurança
    private int minutesSpent;

    protected TaskLog() {} // JPA only

    public TaskLog(Student student, Course course, TaskCategory category,
                   LocalDateTime timestamp, String description, int minutesSpent) {
        this.student = student;
        this.course = course;
        this.category = category;
        this.timestamp = timestamp;
        this.description = description;
        this.minutesSpent = minutesSpent;
    }

    public void update(TaskCategory category, LocalDateTime timestamp, String description, int minutesSpent) {
        this.category = category;
        this.timestamp = timestamp;
        this.description = description;
        this.minutesSpent = minutesSpent;
    }
}
