package br.com.biblioteca.domain.user.enums;

import br.com.biblioteca.core.ExceptionCode;
import lombok.Getter;

@Getter
public enum UserExceptionCodeEnum implements ExceptionCode {

    USER_NOT_FOUND("User not found", "USEREXCEP-001", 404),
    DUPLICATE_USER("Duplicate user found", "USEREXCEP-002", 409),
    DUPLICATE_EMAIL("Duplicate email", "USEREXCEP-003", 409),
    INVALID_EMAIL("Invalid email format or missing", "USEREXCEP-004", 400),
    INVALID_PASSWORD("Invalid password format or missing", "USEREXCEP-005", 400), // Atualizado para 005
    DUPLICATE_CNPJ("CNPJ already exists", "USEREXCEP-006", 409),
    DUPLICATE_CPF("CPF already exists", "USEREXCEP-007", 409),
    DUPLICATE_REGISTRATION("Registration number already exists", "USEREXCEP-008", 409),
    PARTNER_COMPANY_NOT_FOUND("Partner company not found", "USEREXCEP-009", 404),
    EMAIL_DOES_NOT_MATCH("Email does not match", "USEREXCEP-010", 409),
    FILE_UPLOAD_FAILED("Failed to upload", "USEREXCEP-011", 400),
    IMAGE_SIZE_EXCEEDED("The image size exceeds the maximum limit of 5MB.", "USEREXCEP-012", 400);


    private final String message;
    private final String code;
    private final int httpStatus;

    UserExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}