package ge.mg.tasktrackerapi.service.project;

import ge.mg.tasktrackerapi.model.project.common.ProjectDto;
import ge.mg.tasktrackerapi.model.project.request.CreateUpdateProjectDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    Page<ProjectDto> getProjects(Pageable pageable);

    ProjectDto createProject(CreateUpdateProjectDto dto);

    ProjectDto updateProject(CreateUpdateProjectDto dto);

    void deleteProject(Long id);
}
