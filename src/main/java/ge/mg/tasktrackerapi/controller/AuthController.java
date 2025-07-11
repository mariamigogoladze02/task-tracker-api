package ge.mg.tasktrackerapi.controller;

import ge.mg.tasktrackerapi.model.user.request.AuthRequestDTO;
import ge.mg.tasktrackerapi.model.user.request.TokenRefreshRequest;
import ge.mg.tasktrackerapi.model.user.response.AuthResponse;
import ge.mg.tasktrackerapi.service.user.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("authorization")
    public ResponseEntity<AuthResponse> authorization(@Valid @RequestBody AuthRequestDTO authRequestDTO) {
        return ResponseEntity.ok(authService.authorization(authRequestDTO));
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
