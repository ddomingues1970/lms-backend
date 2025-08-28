package com.danieldomingues.lms.web;

import com.danieldomingues.lms.domain.TaskLog;
import com.danieldomingues.lms.service.TaskLogService;
import com.danieldomingues.lms.web.dto.TaskLogCreateDto;
import com.danieldomingues.lms.web.dto.TaskLogUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-logs")
public class TaskLogController {

    private final TaskLogService service;

    public TaskLogController(TaskLogService service) {
        this.service = service;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TaskLog>> list(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.listByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<TaskLog> create(@RequestBody @Valid TaskLogCreateDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskLog> update(@PathVariable Long id, @RequestBody @Valid TaskLogUpdateDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
