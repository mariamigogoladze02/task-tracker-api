package ge.mg.tasktrackerapi.model.user.common;

import ge.mg.tasktrackerapi.persistence.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
