package ge.mg.tasktrackerapi.model.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;
}

