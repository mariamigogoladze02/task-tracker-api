package ge.mg.tasktrackerapi.config.jpa;

import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.service.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {

    private static final Long DEFAULT_USER_ID = -100L;

    private AuthService authService;

    @Override
    public Optional<Long> getCurrentAuditor() {
        UserDetailsImpl userDetails = authService.getUser();

        return Objects.nonNull(userDetails) ? Optional.of(authService.getUser().getId()) : defaultUserId();

    }

    private Optional<Long> defaultUserId() {
        return Optional.of(DEFAULT_USER_ID);
    }

    @Autowired
    public void setAuthenticationService(AuthService authService) {
        this.authService = authService;
    }
}