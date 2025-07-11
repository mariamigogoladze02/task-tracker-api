package ge.mg.tasktrackerapi.model.task.request;

import ge.mg.tasktrackerapi.persistence.entity.enums.TaskPriority;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskFilterDto {
    public TaskStatus status;
    private TaskPriority priority;
}
