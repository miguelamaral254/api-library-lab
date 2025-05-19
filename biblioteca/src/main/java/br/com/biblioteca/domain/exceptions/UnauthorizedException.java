package br.com.biblioteca.domain.exceptions;

import br.com.biblioteca.core.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        this("Unauthorized");
    }
}
