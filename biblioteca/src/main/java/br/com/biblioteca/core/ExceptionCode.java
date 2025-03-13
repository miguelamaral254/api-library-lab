package br.com.biblioteca.core;


public interface ExceptionCode {
    String getCode();
    String getMessage();
    int getHttpStatus();
}