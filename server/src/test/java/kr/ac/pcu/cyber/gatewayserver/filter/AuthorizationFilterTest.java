package kr.ac.pcu.cyber.gatewayserver.filter;

import kr.ac.pcu.cyber.gatewayserver.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class AuthorizationFilterTest {
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

    private AuthorizationFilter authorizationFilter;
    private final JwtUtil jwtUtil = new JwtUtil(SECRET);
    MockServerWebExchange exchange;
    GatewayFilterChain chain = mock(GatewayFilterChain.class);
    private ArgumentCaptor<ServerWebExchange> captor = ArgumentCaptor.forClass(ServerWebExchange.class);


    @BeforeEach
    void setUp() {
        authorizationFilter = new AuthorizationFilter(jwtUtil);
    }

    @Test
    void unauthorized_request_with_INVALID_TOKEN() {
        exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/user-service/test")
                        .cookie(new HttpCookie("access_token", INVALID_TOKEN))
                        .build()
        );

        GatewayFilter filter = authorizationFilter.apply(new AuthorizationFilter.Config());
        filter.filter(exchange, chain);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void unauthorized_request_with_EXPRIED_TOKEN() {
        exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/user-service/test")
                        .cookie(new HttpCookie("access_token", EXPIRED_TOKEN))
                        .build()
        );

        GatewayFilter filter = authorizationFilter.apply(new AuthorizationFilter.Config());
        filter.filter(exchange, chain);

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void authorized_request_with_VALID_TOKEN() {
        exchange = MockServerWebExchange.from(
                MockServerHttpRequest.post("/user-service/test")
                        .cookie(new HttpCookie("access_token", VALID_TOKEN))
                        .build()
        );

        GatewayFilter filter = authorizationFilter.apply(new AuthorizationFilter.Config());
        filter.filter(exchange, chain);

        assertNull(exchange.getResponse().getStatusCode());
    }

}