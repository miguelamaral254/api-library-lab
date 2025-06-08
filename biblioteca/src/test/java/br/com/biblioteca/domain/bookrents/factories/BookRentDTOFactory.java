package br.com.biblioteca.domain.bookrents.factories;

import br.com.biblioteca.domain.bookrent.BookRentDTO;

import br.com.biblioteca.domain.user.factories.UserFactory;

import java.time.LocalDateTime;

public class BookRentDTOFactory {

    public static final Long DEFAULT_ID = 1L;
    public static final Long DEFAULT_BOOK_ID = 1L;
    public static final Long DEFAULT_USER_ID = UserFactory.savedUser().getId();
    public static final LocalDateTime DEFAULT_RETURN_DATE = LocalDateTime.now().plusDays(7);
    public static final Boolean DEFAULT_LATE = false;
    public static final Boolean DEFAULT_ENABLED = true;
    public static final LocalDateTime DEFAULT_CREATED_DATE = LocalDateTime.now();
    public static final LocalDateTime DEFAULT_LAST_MODIFIED_DATE = LocalDateTime.now();

    private BookRentDTOFactory() {}

    public static BookRentDTO savedBookRentDto(Long id, Long bookId, Long userId) {
        return new BookRentDTO(
                id,
                bookId,
                userId,
                DEFAULT_RETURN_DATE,
                DEFAULT_LATE,
                DEFAULT_ENABLED,
                DEFAULT_CREATED_DATE,
                DEFAULT_LAST_MODIFIED_DATE
        );
    }

    public static BookRentDTO savedBookRentDto() {
        return savedBookRentDto(DEFAULT_ID, DEFAULT_BOOK_ID, DEFAULT_USER_ID);
    }
}