package ge.mg.tasktrackerapi.model.project.mapper;

import ge.mg.tasktrackerapi.model.project.common.ProjectDto;
import ge.mg.tasktrackerapi.model.user.mapper.UserMapper;
import ge.mg.tasktrackerapi.persistence.entity.Project;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectMapper {
    private final UserMapper userMapper;

    public ProjectDto mapToDto(Project project) {
        if (project == null) {
            return null;
        }

        User owner = project.getOwner();

        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .owner(userMapper.mapToDto(owner))
                .createdDate(project.getCreatedDate())
                .lastModifiedDate(project.getLastModifiedDate())
                .build();
    }
}
