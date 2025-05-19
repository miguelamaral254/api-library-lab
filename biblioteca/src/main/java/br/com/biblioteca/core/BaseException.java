package br.com.biblioteca.core;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BaseException(String message, int httpStatusCode) {
        this(message, HttpStatus.valueOf(httpStatusCode));
    }
}
