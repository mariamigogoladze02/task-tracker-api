package ge.mg.tasktrackerapi.service.user.impl;

import ge.mg.tasktrackerapi.config.security.TokenProvider;
import ge.mg.tasktrackerapi.exception.AppException;
import ge.mg.tasktrackerapi.exception.ErrorCode;
import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.model.user.common.UserDto;
import ge.mg.tasktrackerapi.model.user.mapper.UserMapper;
import ge.mg.tasktrackerapi.model.user.request.AuthRequestDTO;
import ge.mg.tasktrackerapi.model.user.request.TokenRefreshRequest;
import ge.mg.tasktrackerapi.model.user.response.AuthResponse;
import ge.mg.tasktrackerapi.persistence.entity.user.RefreshToken;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import ge.mg.tasktrackerapi.service.user.AuthService;
import ge.mg.tasktrackerapi.service.user.RefreshTokenService;
import ge.mg.tasktrackerapi.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public UserDetailsImpl getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetailsImpl) {
                return (UserDetailsImpl) principal;
            }
        }

        return null;
    }

    @Override
    public AuthResponse authorization(AuthRequestDTO authRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.getUsername(),
                        authRequestDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserDto userDto = userMapper.mapToDto(userDetails);
        String token = tokenProvider.createToken(userDto);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new AuthResponse(token, refreshToken.getToken());
    }

    @Override
    public void logout(HttpServletRequest request) {
        String jwt = Utils.getJwtFromRequest(request);
        Long userId = tokenProvider.getUserIdFromToken(jwt);

        refreshTokenService.deleteByUserId(userId);
    }

    @Override
    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map((refreshToken -> {
                    User user = refreshToken.getUser();
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    String token = tokenProvider.createToken(userMapper.mapToDto(userDetails));

                    return new AuthResponse(token, requestRefreshToken);
                }))
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }
}
