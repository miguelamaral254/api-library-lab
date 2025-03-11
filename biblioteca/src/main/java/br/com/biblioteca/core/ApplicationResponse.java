package br.com.biblioteca.core;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApplicationResponse<T>(
        T data,
        ErrorMessage error
) {

    public static <T> ApplicationResponse<T> ofSuccess(T data) {
        return new ApplicationResponse<>(data, null);
    }

    public static <T> ApplicationResponse<T> ofError(ErrorMessage error) {
        return new ApplicationResponse<>(null, error);
    }

    public static <T> ApplicationResponse<T> ofError(String code, String message) {
        return ofError(new ErrorMessage(code, message));
    }
}
