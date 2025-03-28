package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.core.ExceptionCode;
import lombok.Getter;

@Getter
public enum BookArtifactExceptionCodeEnum implements ExceptionCode {

    ARTIFACT_NOT_FOUND("Book artifact not found", "ARTIFACTEXCEP-404", 404),
    ARTIFACT_ALREADY_EXISTS("Book artifact already exists", "ARTIFACTEXCEP-409", 409),
    INVALID_ARTIFACT_DESCRIPTION("Invalid artifact description", "ARTIFACTEXCEP-400", 400),
    BOOK_NOT_FOUND("Associated book not found", "ARTIFACTEXCEP-404", 404);

    private final String message;
    private final String code;
    private final int httpStatus;

    BookArtifactExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}