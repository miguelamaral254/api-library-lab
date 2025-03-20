package br.com.biblioteca.domain.book;


import br.com.biblioteca.core.ExceptionCode;
import lombok.Getter;


@Getter
public enum BookExceptionCodeEnum implements ExceptionCode {

    BOOK_NOT_FOUND("BOOK not found", "BOOKEXCEP-404", 404),
    BOOK_ALREADY_EXISTS("BOOK already exists", "BOOKEXCEP-409", 409),
    INVALID_BOOK_TITLE("Invalid book title", "BOOKEXCEP-400", 400),
    INVALID_USER("Invalid user associated with book", "BOOKEXCEP-400", 400),
    USER_NOT_FOUND("User not found", "USEREXCEP-404", 404),
    INVALID_AVAILABILITY_STATUS("Invalid availability status", "BOOKEXCEP-400", 400),
    INVALID_ENABLED_STATUS("Invalid enabled status", "BOOKEXCEP-400", 400);

    private final String message;
    private final String code;
    private final int httpStatus;

    BookExceptionCodeEnum(String message, String code, int httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}