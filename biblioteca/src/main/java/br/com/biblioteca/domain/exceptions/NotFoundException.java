package br.com.biblioteca.domain.exceptions;

import br.com.biblioteca.core.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException() {
        this("Not Found");
    }
}
