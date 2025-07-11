package ge.mg.tasktrackerapi.service.user.impl;


import ge.mg.tasktrackerapi.config.security.AppProperties;
import ge.mg.tasktrackerapi.exception.AppException;
import ge.mg.tasktrackerapi.exception.ErrorCode;
import ge.mg.tasktrackerapi.persistence.entity.user.RefreshToken;
import ge.mg.tasktrackerapi.persistence.repository.user.RefreshTokenRepository;
import ge.mg.tasktrackerapi.persistence.repository.user.UserRepository;
import ge.mg.tasktrackerapi.service.user.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final AppProperties appProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        RefreshToken refreshToken = null;
        boolean createNew = true;
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findFirstByUser_Id(userId);
        if (refreshTokenOptional.isPresent()) {
            createNew = false;
            refreshToken = refreshTokenOptional.get();
            LocalDateTime expiryDate = refreshToken.getExpiryDate();
            if (expiryDate.isBefore(now)) {
                refreshTokenRepository.delete(refreshToken);
                refreshTokenRepository.flush();
                createNew = true;
            }
        }

        if (createNew) {
            refreshToken = new RefreshToken();

            refreshToken.setUser(userRepository.findById(userId).get());
        }

        refreshToken.setExpiryDate(now.plus(appProperties.getAuth().getJwtRefreshExpirationMs(), ChronoUnit.MILLIS));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);


        return refreshToken;
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

    @Transactional
    @Override
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
