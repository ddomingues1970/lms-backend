package com.danieldomingues.lms.config;

import com.danieldomingues.lms.domain.TaskCategory;
import com.danieldomingues.lms.repository.TaskCategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskCategorySeeder {

    private final TaskCategoryRepository repo;

    public TaskCategorySeeder(TaskCategoryRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void seedCategories() {
        List<String> categories = List.of("PESQUISA", "PRATICA", "ASSISTIR_VIDEOAULA");
        for (String name : categories) {
            repo.findByName(name).orElseGet(() -> repo.save(new TaskCategory(name)));
        }
    }
}
