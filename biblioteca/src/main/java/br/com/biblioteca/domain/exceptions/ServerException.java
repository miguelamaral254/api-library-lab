package br.com.biblioteca.domain.exceptions;

import br.com.biblioteca.core.BaseException;
import org.springframework.http.HttpStatus;

public class ServerException extends BaseException {
    public ServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ServerException() {
        this("Already Exists");
    }
}
