package ge.mg.tasktrackerapi.controller;

import ge.mg.tasktrackerapi.model.task.common.TaskDto;
import ge.mg.tasktrackerapi.model.task.request.CreateUpdateTaskDto;
import ge.mg.tasktrackerapi.model.task.request.TaskFilterDto;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import ge.mg.tasktrackerapi.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/task")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_MANAGER')")
    public ResponseEntity<Page<TaskDto>> getTasks(TaskFilterDto dto, Pageable pageable) {
        return ResponseEntity.ok(taskService.getTasks(dto, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateUpdateTaskDto dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<TaskDto> updateTask(@RequestBody CreateUpdateTaskDto dto) {
        return ResponseEntity.ok(taskService.updateTask(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<TaskDto> assignTask(@PathVariable Long id, @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.assignTask(id, userId));
    }
}
