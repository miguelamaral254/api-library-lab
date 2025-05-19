package br.com.biblioteca.domain.exceptions;

import br.com.biblioteca.core.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException() {
        this("Already Exists");
    }
}
