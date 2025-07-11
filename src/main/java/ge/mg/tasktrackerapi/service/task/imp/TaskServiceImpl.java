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
import ge.mg.tasktrackerapi.persistence.entity.enums.TaskStatus;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import ge.mg.tasktrackerapi.persistence.repository.ProjectRepository;
import ge.mg.tasktrackerapi.persistence.repository.TaskRepository;
import ge.mg.tasktrackerapi.persistence.repository.user.UserRepository;
import ge.mg.tasktrackerapi.service.task.TaskService;
import ge.mg.tasktrackerapi.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final AuthService authService;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public Page<TaskDto> getTasks(TaskFilterDto dto, Pageable pageable) {
        UserDetailsImpl user = authService.getUser();
        Role role = user.getRole();

        if (Objects.equals(role, Role.USER)) {
            return taskRepository.getUserTasks(
                            user.getId(),
                            dto.getStatus() == null ? null : dto.getStatus().name(),
                            dto.getPriority() == null ? null : dto.getPriority().name(),
                            pageable)
                    .map(taskMapper::mapToDto);
        } else if (Objects.equals(role, Role.MANAGER)) {
            return taskRepository.getManagerTasks(
                            user.getId(),
                            dto.getStatus() == null ? null : dto.getStatus().name(),
                            dto.getPriority() == null ? null : dto.getPriority().name(),
                            pageable)
                    .map(taskMapper::mapToDto);
        }

        return null;
    }

    @Transactional
    @Override
    public TaskDto createTask(CreateUpdateTaskDto dto) {
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        User assignedUser = userRepository.findById(dto.getAssignedUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(TaskStatus.TODO)
                .priority(dto.getPriority())
                .dueDate(dto.getDueDate())
                .project(project)
                .assignedUser(assignedUser)
                .build();

        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @Transactional
    @Override
    public TaskDto updateTask(CreateUpdateTaskDto dto) {
        Long id = dto.getId();
        if (id == null) {
            throw new AppException(ErrorCode.BAD_REQUEST_ID_MUST_NOT_BE_NULL);
        }
        Task task = taskRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());

        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Transactional
    @Override
    public TaskDto updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        task.setStatus(status);
        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @Transactional
    @Override
    public TaskDto assignTask(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        task.setAssignedUser(user);
        return taskMapper.mapToDto(taskRepository.save(task));
    }

}
