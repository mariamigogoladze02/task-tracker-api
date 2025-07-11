package ge.mg.tasktrackerapi.config.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @PostConstruct
    public void init() {
        this.auth.setTokenSecret(jwtSecret);
        this.auth.setTokenExpirationMs(jwtExpirationMs);
        this.auth.setJwtRefreshExpirationMs(jwtRefreshExpirationMs);
    }

    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMs;
        private long jwtRefreshExpirationMs;
    }

}
