package ge.mg.tasktrackerapi.model.task.common;

import ge.mg.tasktrackerapi.model.project.common.ProjectDto;
import ge.mg.tasktrackerapi.model.user.common.UserDto;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskPriority;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private TaskPriority priority;
    private ProjectDto project;
    private UserDto assignedUser;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
