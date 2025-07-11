package ge.mg.tasktrackerapi.model.user.mapper;

import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.model.user.common.UserDto;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto mapToDto(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return null;
        }
        return UserDto.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .password(userDetails.getPassword())
                .role(userDetails.getRole())
                .createdDate(userDetails.getCreatedDate())
                .lastModifiedDate(userDetails.getLastModifiedDate())
                .build();
    }

    public UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }
}
