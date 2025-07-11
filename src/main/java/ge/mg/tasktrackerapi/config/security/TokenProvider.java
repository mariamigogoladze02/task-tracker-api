package ge.mg.tasktrackerapi.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.mg.tasktrackerapi.model.user.common.UserDetailsImpl;
import ge.mg.tasktrackerapi.model.user.common.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenProvider {
    private final static String ALGORITHM = "HmacSHA512";

    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String createToken(UserDto userDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMs());

        Key secretKey = new SecretKeySpec(appProperties.getAuth().getTokenSecret().getBytes(), "HmacSHA512");

        return Jwts.builder()
                .subject(Long.toString(userDto.getId()))
                .claim("user", objectMapper.writeValueAsString(userDto))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    @SneakyThrows
    public UserDetails getUserDetailsFromToken(String token) {
        SecretKey secretKey = new SecretKeySpec(appProperties.getAuth().getTokenSecret().getBytes(), ALGORITHM);

        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        String userJson = (String) claims.get("user");

        UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
        return new UserDetailsImpl(userDto);
    }

    public Long getUserIdFromToken(String token) {
        SecretKey secretKey = new SecretKeySpec(appProperties.getAuth().getTokenSecret().getBytes(), ALGORITHM);

        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        Claims claims = parser.parseSignedClaims(token).getPayload();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            SecretKey secretKey = new SecretKeySpec(appProperties.getAuth().getTokenSecret().getBytes(), ALGORITHM);

            JwtParser parser = Jwts.parser()
                    .verifyWith(secretKey)
                    .build();

            parser.parseSignedClaims(authToken);

            return true;
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
