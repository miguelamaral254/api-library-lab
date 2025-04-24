package br.com.biblioteca.domain.user.enums;

import br.com.biblioteca.core.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExceptionCodeEnum implements ExceptionCode {

    USER_NOT_FOUND("User not found", "USEREXCEP-001", 404),
    DUPLICATE_USER("Duplicate user found", "USEREXCEP-002", 409),
    DUPLICATE_EMAIL("Duplicate email", "USEREXCEP-003", 409),
    INVALID_EMAIL("Invalid email format or missing", "USEREXCEP-004", 400),
    INVALID_PASSWORD("Invalid password format or missing", "USEREXCEP-005", 400),
    DUPLICATE_CPF("CPF already exists", "USEREXCEP-007", 409),
    EMAIL_AND_PASSWORD_DOES_NOT_MATCH("Email and Password does not match", "USEREXCEP-010", 409),
    INVALID_NAME("Name cannot be empty or contain invalid characters", "USEREXCEP-400", 400),
    INVALID_CPF("Invalid CPF format", "USEREXCEP-400", 400),
    INVALID_ROLE("Invalid role or does not exists", "USEREXCEP-400", 400),
    INVALID_INSTITUTION("Invalid institution or does not exists", "USEREXCEP-404", 404),
    INVALID_COURSE("Invalid course or does not exists", "USEREXCEP-404", 404),
    INVALID_PHONE("Invalid phone format", "USEREXCEP-400", 400),
    FILE_UPLOAD_FAILED("Failed to upload", "USEREXCEP-011", 400),
    IMAGE_SIZE_EXCEEDED("The image size exceeds the maximum limit of 5MB.", "USEREXCEP-012", 400);

    private final String message;
    private final String code;
    private final int httpStatus;

}