package ge.mg.tasktrackerapi.model.project.common;


import ge.mg.tasktrackerapi.model.user.common.UserDto;
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
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
