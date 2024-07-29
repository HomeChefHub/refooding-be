package refooding.api.common.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(
        HttpStatus status,
        String message
) {
    public static ExceptionResponse of(ExceptionCode exceptionCode) {
        return new ExceptionResponse(exceptionCode.getStatus(), exceptionCode.getMessage());
    }

    public static ExceptionResponse of(HttpStatus status, String message) {
        return new ExceptionResponse(status, message);
    }
}
