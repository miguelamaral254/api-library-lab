package br.com.biblioteca.domain.errors.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
    private final String code;
    private final String message;
    private final int httpStatus;

    // Construtor para UserExceptionCodeEnum
    public BusinessException(UserExceptionCodeEnum userError) {
        super(userError.getMessage());
        this.code = userError.getCode();
        this.message = userError.getMessage();
        this.httpStatus = userError.getHttpStatus();
    }

    // Construtor para ProjectExceptionCodeEnum
    public BusinessException(ProjectExceptionCodeEnum projectError) {
        super(projectError.getMessage());
        this.code = projectError.getCode();
        this.message = projectError.getMessage();
        this.httpStatus = projectError.getHttpStatus();
    }

    // Construtor para GeneralExceptionCodeEnum
    public BusinessException(GeneralExceptionCodeEnum generalError) {
        super(generalError.getMessage());
        this.code = generalError.getCode();
        this.message = generalError.getMessage();
        this.httpStatus = generalError.getHttpStatus();
    }
}