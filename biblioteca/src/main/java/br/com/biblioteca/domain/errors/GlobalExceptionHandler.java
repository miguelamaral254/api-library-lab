package br.com.biblioteca.domain.errors;


import br.com.biblioteca.domain.errors.exceptions.BusinessException;
import br.com.biblioteca.domain.errors.exceptions.GeneralExceptionCodeEnum;
import br.com.biblioteca.domain.errors.exceptions.ProjectExceptionCodeEnum;
import br.com.biblioteca.domain.errors.exceptions.UserExceptionCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        if (ex.getCode().startsWith("USEREXCEP")) {
            return ResponseEntity.status(HttpStatus.valueOf(UserExceptionCodeEnum.valueOf(ex.getCode()).getHttpStatus()))
                    .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
        } else if (ex.getCode().startsWith("PROJEXCEP")) {
            return ResponseEntity.status(HttpStatus.valueOf(ProjectExceptionCodeEnum.valueOf(ex.getCode()).getHttpStatus()))
                    .body(new ErrorResponse(ex.getCode(), ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.valueOf(GeneralExceptionCodeEnum.SERVER_ERROR.getHttpStatus()))
                .body(new ErrorResponse(GeneralExceptionCodeEnum.SERVER_ERROR.getCode(), GeneralExceptionCodeEnum.SERVER_ERROR.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.valueOf(GeneralExceptionCodeEnum.SERVER_ERROR.getHttpStatus()))
                .body(new ErrorResponse(GeneralExceptionCodeEnum.SERVER_ERROR.getCode(), GeneralExceptionCodeEnum.SERVER_ERROR.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse {
        private final String code;
        private final String message;
    }
}