package com.danieldomingues.lms.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "task_categories")
@Getter
public class TaskCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    protected TaskCategory() {} // JPA only

    public TaskCategory(String name) {
        this.name = name;
    }
}
