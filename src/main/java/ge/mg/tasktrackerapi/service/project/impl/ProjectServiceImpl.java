package ge.mg.tasktrackerapi.service.project.impl;

import ge.mg.tasktrackerapi.exception.AppException;
import ge.mg.tasktrackerapi.exception.ErrorCode;
import ge.mg.tasktrackerapi.model.project.common.ProjectDto;
import ge.mg.tasktrackerapi.model.project.mapper.ProjectMapper;
import ge.mg.tasktrackerapi.model.project.request.CreateUpdateProjectDto;
import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.persistence.entity.Project;
import ge.mg.tasktrackerapi.persistence.entity.enums.Role;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import ge.mg.tasktrackerapi.persistence.repository.ProjectRepository;
import ge.mg.tasktrackerapi.persistence.repository.user.UserRepository;
import ge.mg.tasktrackerapi.service.project.ProjectService;
import ge.mg.tasktrackerapi.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthService authService;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;

    @Override
    public Page<ProjectDto> getProjects(Pageable pageable) {
        UserDetailsImpl user = authService.getUser();
        Role role = user.getRole();

        if (Objects.equals(role, Role.ADMIN)) {
            return projectRepository.findAll(pageable)
                    .map(projectMapper::mapToDto);
        } else if (Objects.equals(role, Role.MANAGER)) {
            return projectRepository.findAllByOwner_Id(user.getId(), pageable)
                    .map(projectMapper::mapToDto);
        }

        return null;
    }

    @Transactional
    @Override
    public ProjectDto createProject(CreateUpdateProjectDto dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        project.setOwner(owner);

        project = projectRepository.save(project);
        return projectMapper.mapToDto(project);
    }

    @Transactional
    @Override
    public ProjectDto updateProject(CreateUpdateProjectDto dto) {
        Long id = dto.getId();
        if (id == null) {
            throw new AppException(ErrorCode.BAD_REQUEST_ID_MUST_NOT_BE_NULL);
        }

        Project project = projectRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        project.setOwner(owner);

        project = projectRepository.save(project);
        return projectMapper.mapToDto(project);
    }

    @Transactional
    @Override
    public void deleteProject(Long id) {
        UserDetailsImpl currentUser = authService.getUser();

        Project project = projectRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if (!Objects.equals(project.getOwner().getId(), currentUser.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        projectRepository.delete(project);
    }
}
