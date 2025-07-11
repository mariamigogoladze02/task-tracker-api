package ge.mg.tasktrackerapi.service.user;

import ge.mg.tasktrackerapi.model.user.common.UserDto;
import ge.mg.tasktrackerapi.model.user.request.CreateUserDto;
import jakarta.validation.Valid;

public interface UserService {
    UserDto create(@Valid CreateUserDto dto);
}
