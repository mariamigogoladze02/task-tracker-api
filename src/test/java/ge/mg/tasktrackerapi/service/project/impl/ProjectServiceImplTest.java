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
import ge.mg.tasktrackerapi.service.user.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ProjectMapper projectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProjects_asAdmin_returnsAllProjects() {
        User admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@mail.com");
        admin.setPassword("pass");
        admin.setRole(Role.ADMIN);

        UserDetailsImpl adminUser = new UserDetailsImpl(admin);
        Pageable pageable = PageRequest.of(0, 10);
        Project project = new Project();
        ProjectDto projectDto = new ProjectDto();

        when(authService.getUser()).thenReturn(adminUser);
        when(projectRepository.findAll(pageable)).thenReturn(new PageImpl<>(java.util.List.of(project)));
        when(projectMapper.mapToDto(project)).thenReturn(projectDto);

        Page<ProjectDto> result = projectService.getProjects(pageable);

        assertEquals(1, result.getTotalElements());
        verify(projectRepository).findAll(pageable);
    }

    @Test
    void getProjects_asManager_returnsOwnProjects() {
        User manager = new User();
        manager.setId(1L);
        manager.setEmail("manager@mail.com");
        manager.setPassword("pass");
        manager.setRole(Role.MANAGER);

        UserDetailsImpl managerUser = new UserDetailsImpl(manager);
        Pageable pageable = PageRequest.of(0, 10);
        Project project = new Project();
        ProjectDto projectDto = new ProjectDto();

        when(authService.getUser()).thenReturn(managerUser);
        when(projectRepository.findAllByOwner_Id(manager.getId(), pageable))
                .thenReturn(new PageImpl<>(java.util.List.of(project)));
        when(projectMapper.mapToDto(project)).thenReturn(projectDto);

        Page<ProjectDto> result = projectService.getProjects(pageable);

        assertEquals(1, result.getTotalElements());
        verify(projectRepository).findAllByOwner_Id(manager.getId(), pageable);
    }

    @Test
    void createProject_success() {
        CreateUpdateProjectDto dto = new CreateUpdateProjectDto();
        User owner = new User();
        Project saved = new Project();
        ProjectDto response = new ProjectDto();

        when(userRepository.findById(dto.getOwnerId())).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(saved);
        when(projectMapper.mapToDto(saved)).thenReturn(response);

        ProjectDto result = projectService.createProject(dto);

        assertNotNull(result);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void createProject_userNotFound_throwsException() {
        CreateUpdateProjectDto dto = new CreateUpdateProjectDto();
        when(userRepository.findById(dto.getOwnerId())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> projectService.createProject(dto));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateProject_success() {
        CreateUpdateProjectDto dto = new CreateUpdateProjectDto();
        dto.setId(1L);
        dto.setName("Updated");
        dto.setDescription("Updated Desc");
        dto.setOwnerId(1L);

        Project existing = new Project();
        User newOwner = new User();
        Project saved = new Project();
        ProjectDto resultDto = new ProjectDto();

        when(projectRepository.findById(dto.getId())).thenReturn(Optional.of(existing));
        when(userRepository.findById(dto.getOwnerId())).thenReturn(Optional.of(newOwner));
        when(projectRepository.save(existing)).thenReturn(saved);
        when(projectMapper.mapToDto(saved)).thenReturn(resultDto);

        ProjectDto result = projectService.updateProject(dto);

        assertNotNull(result);
    }


    @Test
    void updateProject_projectNotFound_throwsException() {
        CreateUpdateProjectDto dto = new CreateUpdateProjectDto();
        dto.setId(99L);
        dto.setName("name");
        dto.setDescription("desc");
        dto.setOwnerId(1L);

        when(projectRepository.findById(dto.getId())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> projectService.updateProject(dto));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void updateProject_userNotFound_throwsException() {
        CreateUpdateProjectDto dto = new CreateUpdateProjectDto();
        dto.setId(1L);
        dto.setName("name");
        dto.setDescription("desc");
        dto.setOwnerId(999L);

        Project existing = new Project();

        when(projectRepository.findById(dto.getId())).thenReturn(Optional.of(existing));
        when(userRepository.findById(dto.getOwnerId())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> projectService.updateProject(dto));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void deleteProject_success() {
        Long projectId = 1L;
        Long userId = 42L;

        UserDetailsImpl mockUser = mock(UserDetailsImpl.class);
        when(mockUser.getId()).thenReturn(userId);

        User owner = new User();
        owner.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setOwner(owner);

        when(authService.getUser()).thenReturn(mockUser);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId);
        verify(projectRepository).delete(project);
    }
}
