package kr.ac.pcu.cyber.gatewayserver.util;

import io.jsonwebtoken.Claims;
import kr.ac.pcu.cyber.gatewayserver.exception.InvalidTokenException;
import kr.ac.pcu.cyber.gatewayserver.exception.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "12345678901234567890123456789012";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOiIyZjQ4ZjI0MS05ZDY0LTRkMTYtYmY1Ni03MGI5ZDRlMGU3OWEifQ." +
            "diJ35TNZtRqYIkkiUZX0JC0IQ_Yia8c5p8FDd_FMgYo";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1dWlkIjoiMmY0OGYyNDEtOWQ2NC00ZDE2LWJmNTYtNzBiOWQ0ZTBlNzlhIiwiZXhwIjoxNjIyNjM2Nzc3fQ.J" +
            "gzsO-ovbRtts6ufTaix37R12T5Ngqd4cnxIgOQIxJ1";

    private static final String EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOiIyZjQ4ZjI0MS05ZDY0LTRkMTYtYmY1Ni03MGI5ZDRlMGU3OWEiLCJleHAiOjE2MjI2MzE2Nzd9." +
            "75VsVNYXpd7_SZDS1jZFh_4LVaFmRzZmW5XFfpWKC5g";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void validate_token_valid() {
        try {
            jwtUtil.validateToken(VALID_TOKEN);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void validate_token_invalid_token() {
        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> jwtUtil.validateToken(INVALID_TOKEN)
        );

        assertNotNull(exception.getMessage());
    }

    @Test
    void validate_token_invalid_expired_token() {
        TokenExpiredException exception = assertThrows(
                TokenExpiredException.class,
                () -> jwtUtil.validateToken(EXPIRED_TOKEN)
        );

        assertNotNull(exception.getMessage());
    }

}