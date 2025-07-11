package ge.mg.tasktrackerapi.persistence.repository.user;

import ge.mg.tasktrackerapi.persistence.entity.user.RefreshToken;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findFirstByUser_Id(Long userId);

    @Modifying
    int deleteByUser(User user);
}