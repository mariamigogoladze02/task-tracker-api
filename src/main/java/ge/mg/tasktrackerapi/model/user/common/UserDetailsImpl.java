package ge.mg.tasktrackerapi.model.user.common;

import ge.mg.tasktrackerapi.persistence.entity.enums.Role;
import ge.mg.tasktrackerapi.persistence.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String email;

    private String password;

    private List<GrantedAuthority> authorities;

    private Role role;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;


    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRole() == null ? null : List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.role = user.getRole();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedDate = user.getLastModifiedDate();
    }

    public UserDetailsImpl(UserDto user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.authorities = user.getRole() == null ? null : List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.role = user.getRole();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedDate = user.getLastModifiedDate();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
