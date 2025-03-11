package br.com.biblioteca.domain.errors.exceptions;

import lombok.Getter;

@Getter
public enum ProjectExceptionCodeEnum {

    PROJECT_NOT_FOUND("Project not found", "PROJEXCEP-001", 404),
    PROJECT_ALREADY_EXISTS("Project already exists", "PROJEXCEP-002", 409),
    AUTHOR_NOT_FOUND("Author not found", "PROJEXCEP-003", 404),
    PROJECT_CREATION_FAILED("Failed to create project", "PROJEXCEP-004", 500),
    PROJECT_UPDATE_FAILED("Failed to update project", "PROJEXCEP-005", 500),
    INVALID_PROJECT_DATA("The project data is missing or incomplete", "PROJEXCEP-006", 400),
    INVALID_PROJECT_TITLE("The project title is required", "PROJEXCEP-007", 400),
    INVALID_PROJECT_DESCRIPTION("The project description is required", "PROJEXCEP-008", 400),
    INVALID_PARTNER_COMPANY("A valid partner company is required for this project", "PROJEXCEP-009", 400),
    IMAGE_CREATION_FAILED("Failed to save image", "PROJEXCEP-010", 400);

    private final String message;
    private final String code;
    private final int httpStatus;

    ProjectExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}