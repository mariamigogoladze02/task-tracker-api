package ge.mg.tasktrackerapi.service.user;


import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.model.user.request.AuthRequestDTO;
import ge.mg.tasktrackerapi.model.user.request.TokenRefreshRequest;
import ge.mg.tasktrackerapi.model.user.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthService {
    UserDetailsImpl getUser();

    AuthResponse authorization(AuthRequestDTO authRequestDTO);

    void logout(HttpServletRequest request);

    AuthResponse refreshToken(@Valid TokenRefreshRequest request);

}
