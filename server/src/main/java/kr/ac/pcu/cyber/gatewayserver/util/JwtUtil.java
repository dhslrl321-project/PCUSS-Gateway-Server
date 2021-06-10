package kr.ac.pcu.cyber.gatewayserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import kr.ac.pcu.cyber.gatewayserver.exception.InvalidTokenException;
import kr.ac.pcu.cyber.gatewayserver.exception.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public void validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("해당 userId: " + claims.get("userId") + "는 검증된 userId 입니다.");

        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(token);
        } catch (SignatureException e) {
            throw new InvalidTokenException(token);
        }
    }
}
