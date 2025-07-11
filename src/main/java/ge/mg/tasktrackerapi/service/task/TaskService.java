package ge.mg.tasktrackerapi.service.task;

import ge.mg.tasktrackerapi.model.task.common.TaskDto;
import ge.mg.tasktrackerapi.model.task.request.CreateUpdateTaskDto;
import ge.mg.tasktrackerapi.model.task.request.TaskFilterDto;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Page<TaskDto> getTasks(TaskFilterDto dto, Pageable pageable);

    TaskDto createTask(CreateUpdateTaskDto dto);

    TaskDto updateTask(CreateUpdateTaskDto dto);

    void deleteTask(Long id);

    TaskDto updateTaskStatus(Long id, TaskStatus status);

    TaskDto assignTask(Long id, Long userId);
}
