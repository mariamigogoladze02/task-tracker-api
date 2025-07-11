package ge.mg.tasktrackerapi;

import ge.mg.tasktrackerapi.persistence.entity.enums.Role;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import ge.mg.tasktrackerapi.persistence.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DemoData {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @PostConstruct
    public void store() {
        //todo only for test delete for production

        User admin = new User();
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("test"));
        admin.setRole(Role.ADMIN);

        User manager = new User();
        manager.setEmail("manager@mail.com");
        manager.setPassword(passwordEncoder.encode("test"));
        manager.setRole(Role.MANAGER);

        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword(passwordEncoder.encode("test"));
        user.setRole(Role.USER);

        userRepository.save(admin);
        userRepository.save(manager);
        userRepository.save(user);
    }
}

