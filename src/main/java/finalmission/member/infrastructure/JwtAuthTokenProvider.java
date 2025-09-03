package finalmission.member.infrastructure;

import finalmission.member.domain.AuthTokenProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthTokenProvider implements AuthTokenProvider {

    private static final String TOKEN_TYPE = "Bearer ";

    private final Key key;
    private final int expirationTime;

    public JwtAuthTokenProvider(
            @Value("${auth.token.key}") final String key,
            @Value("${auth.token.expiration}") final int expirationTime
    ) {
        final byte[] encode = Base64.getEncoder().encode(key.getBytes(StandardCharsets.UTF_8));
        this.key = Keys.hmacShaKeyFor(encode);
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(final String email) {
        return TOKEN_TYPE + Jwts.builder()
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setSubject(email)
                .compact();
    }

    public boolean isValidJwt(final String jwt){
        try{
            final String token = extractToken(jwt);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (final JwtException e){
            return false;
        }
    }

    public String extractSubject(final String jwt){
        final String token = extractToken(jwt);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String extractToken(final String jwt) {
        return jwt.replace(TOKEN_TYPE, "");
    }
}
