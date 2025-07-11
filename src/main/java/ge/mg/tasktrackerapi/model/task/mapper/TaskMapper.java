package ge.mg.tasktrackerapi.model.task.mapper;

import ge.mg.tasktrackerapi.model.project.mapper.ProjectMapper;
import ge.mg.tasktrackerapi.model.task.common.TaskDto;
import ge.mg.tasktrackerapi.model.user.mapper.UserMapper;
import ge.mg.tasktrackerapi.persistence.entity.Project;
import ge.mg.tasktrackerapi.persistence.entity.Task;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskMapper {
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public TaskDto mapToDto(Task task) {
        if (task == null) {
            return null;
        }
        Project project = task.getProject();
        User assignedUser = task.getAssignedUser();
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .project(projectMapper.mapToDto(project))
                .assignedUser(userMapper.mapToDto(assignedUser))
                .createdDate(task.getCreatedDate())
                .lastModifiedDate(task.getLastModifiedDate())
                .build();
    }
}
