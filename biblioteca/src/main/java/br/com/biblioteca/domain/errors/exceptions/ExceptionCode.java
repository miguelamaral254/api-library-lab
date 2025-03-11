package br.com.biblioteca.domain.errors.exceptions;

public interface ExceptionCode {
    String getCode();
    String getMessage();
    int getHttpStatus();
}