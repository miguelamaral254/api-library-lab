package br.com.biblioteca.core;

import lombok.Getter;

@Getter
public enum GeneralExceptionCodeEnum implements ExceptionCode {

    SERVER_ERROR("Internal server error", "ERR-500", 500),
    INVALID_TOKEN("Invalid token", "ERR-401", 401),
    INVALID_REQUEST("Invalid request parameters", "ERR-400", 400);

    private final String message;
    private final String code;
    private final int httpStatus;

    GeneralExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}