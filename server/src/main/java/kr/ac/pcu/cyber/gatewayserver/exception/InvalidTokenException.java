package kr.ac.pcu.cyber.gatewayserver.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super(token);
    }
}
