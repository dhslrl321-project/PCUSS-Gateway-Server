package kr.ac.pcu.cyber.gatewayserver.filter;

import kr.ac.pcu.cyber.gatewayserver.common.TokenType;
import kr.ac.pcu.cyber.gatewayserver.exception.InvalidTokenException;
import kr.ac.pcu.cyber.gatewayserver.exception.TokenExpiredException;
import kr.ac.pcu.cyber.gatewayserver.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    private final JwtUtil jwtUtil;

    public AuthorizationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            HttpCookie cookie = request.getCookies().get(String.valueOf(TokenType.ACCESS_TOKEN)).get(0);

            try {
                jwtUtil.validateToken(cookie.getValue());
            } catch (InvalidTokenException | TokenExpiredException e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
