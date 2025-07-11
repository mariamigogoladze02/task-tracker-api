package ge.mg.tasktrackerapi.model.task.request;

import ge.mg.tasktrackerapi.persistence.entity.enums.TaskPriority;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateUpdateTaskDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    @NotBlank
    private TaskPriority priority;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private Long projectId;
    @NotNull
    private Long assignedUserId;
}
