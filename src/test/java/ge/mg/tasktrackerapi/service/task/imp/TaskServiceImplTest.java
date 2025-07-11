package ge.mg.tasktrackerapi.service.task.imp;

import ge.mg.tasktrackerapi.exception.AppException;
import ge.mg.tasktrackerapi.exception.ErrorCode;
import ge.mg.tasktrackerapi.model.task.common.TaskDto;
import ge.mg.tasktrackerapi.model.task.mapper.TaskMapper;
import ge.mg.tasktrackerapi.model.task.request.CreateUpdateTaskDto;
import ge.mg.tasktrackerapi.model.task.request.TaskFilterDto;
import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.persistence.entity.Project;
import ge.mg.tasktrackerapi.persistence.entity.Task;
import ge.mg.tasktrackerapi.persistence.entity.enums.Role;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskPriority;
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import ge.mg.tasktrackerapi.persistence.repository.ProjectRepository;
import ge.mg.tasktrackerapi.persistence.repository.TaskRepository;
import ge.mg.tasktrackerapi.persistence.repository.user.UserRepository;
import ge.mg.tasktrackerapi.service.user.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AuthService authService;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTasks_asUser_callsUserTaskRepositoryMethod() {
        UserDetailsImpl user = mock(UserDetailsImpl.class);
        TaskFilterDto filterDto = new TaskFilterDto();
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        TaskDto taskDto = new TaskDto();

        when(authService.getUser()).thenReturn(user);
        when(user.getRole()).thenReturn(Role.USER);
        when(user.getId()).thenReturn(1L);

        filterDto.setStatus(null);
        filterDto.setPriority(null);

        when(taskRepository.getUserTasks(1L, null, null, pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.mapToDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getTasks(filterDto, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).getUserTasks(1L, null, null, pageable);
    }


    @Test
    void getTasks_asManager_callsManagerTaskRepositoryMethod() {
        UserDetailsImpl user = mock(UserDetailsImpl.class);
        TaskFilterDto filterDto = new TaskFilterDto();
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task();
        TaskDto taskDto = new TaskDto();

        when(authService.getUser()).thenReturn(user);
        when(user.getRole()).thenReturn(Role.MANAGER);
        when(user.getId()).thenReturn(2L);

        filterDto.setStatus(TaskStatus.TODO);
        filterDto.setPriority(null);

        when(taskRepository.getManagerTasks(2L, "TODO", null, pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(taskMapper.mapToDto(task)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.getTasks(filterDto, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository).getManagerTasks(2L, "TODO", null, pageable);
    }


    @Test
    void getTasks_otherRole_returnsNull() {
        UserDetailsImpl user = mock(UserDetailsImpl.class);
        TaskFilterDto filterDto = new TaskFilterDto();
        Pageable pageable = PageRequest.of(0, 10);

        when(authService.getUser()).thenReturn(user);
        when(user.getRole()).thenReturn(Role.ADMIN);

        Page<TaskDto> result = taskService.getTasks(filterDto, pageable);
        assertNull(result);
    }

    @Test
    void createTask_success() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setTitle("Task 1");
        dto.setDescription("Desc");
        dto.setPriority(TaskPriority.MEDIUM);
        dto.setDueDate(LocalDate.now().plusDays(5));
        dto.setProjectId(1L);
        dto.setAssignedUserId(2L);

        Project project = new Project();
        User assignedUser = new User();
        Task task = Task.builder().build();
        Task savedTask = Task.builder().build();
        TaskDto taskDto = new TaskDto();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignedUser));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(taskMapper.mapToDto(savedTask)).thenReturn(taskDto);

        TaskDto result = taskService.createTask(dto);

        assertNotNull(result);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_projectNotFound_throws() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setProjectId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.createTask(dto));
        assertEquals(ErrorCode.PROJECT_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void createTask_userNotFound_throws() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setProjectId(1L);
        dto.setAssignedUserId(2L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project()));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.createTask(dto));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void updateTask_success() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setId(1L);
        dto.setTitle("Updated Task");
        dto.setDescription("Updated Desc");
        dto.setPriority(TaskPriority.HIGH);
        dto.setDueDate(LocalDate.now().plusDays(10));

        Task existing = new Task();
        Task saved = new Task();
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(saved);
        when(taskMapper.mapToDto(saved)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(dto);

        assertNotNull(result);
        verify(taskRepository).save(existing);
    }

    @Test
    void updateTask_idNull_throws() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setId(null);

        AppException ex = assertThrows(AppException.class, () -> taskService.updateTask(dto));
        assertEquals(ErrorCode.BAD_REQUEST_ID_MUST_NOT_BE_NULL, ex.getErrorCode());
    }

    @Test
    void updateTask_notFound_throws() {
        CreateUpdateTaskDto dto = new CreateUpdateTaskDto();
        dto.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.updateTask(dto));
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void deleteTask_callsRepository() {
        Long id = 1L;
        taskService.deleteTask(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    void updateTaskStatus_success() {
        Long userId = 1L;
        Long projectId = 10L;

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getId()).thenReturn(userId);

        User owner = new User();
        owner.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        project.setOwner(owner);

        Task task = new Task();
        task.setId(100L);
        task.setProject(project);

        Task saved = new Task();
        saved.setStatus(TaskStatus.IN_PROGRESS);
        TaskDto taskDto = new TaskDto();

        when(authService.getUser()).thenReturn(userDetails);
        when(taskRepository.findById(100L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(saved);
        when(taskMapper.mapToDto(saved)).thenReturn(taskDto);

        TaskDto result = taskService.updateTaskStatus(100L, TaskStatus.IN_PROGRESS);

        assertNotNull(result);
        verify(taskRepository).save(task);
        assertEquals(TaskStatus.IN_PROGRESS, saved.getStatus());
    }


    @Test
    void updateTaskStatus_notFound_throws() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.updateTaskStatus(1L, TaskStatus.DONE));
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void assignTask_success() {
        Task task = new Task();
        User user = new User();
        Task saved = new Task();
        TaskDto taskDto = new TaskDto();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(saved);
        when(taskMapper.mapToDto(saved)).thenReturn(taskDto);

        TaskDto result = taskService.assignTask(1L, 2L);

        assertNotNull(result);
        verify(taskRepository).save(task);
        assertEquals(user, task.getAssignedUser());
    }

    @Test
    void assignTask_taskNotFound_throws() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.assignTask(1L, 2L));
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void assignTask_userNotFound_throws() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> taskService.assignTask(1L, 2L));
        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
    }
}
