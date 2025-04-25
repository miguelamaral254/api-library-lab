package br.com.biblioteca.domain.books.factories;

import br.com.biblioteca.domain.book.BookDTO;
import br.com.biblioteca.domain.book.Gender;
import br.com.biblioteca.domain.user.factories.UserFactory;

import java.time.LocalDateTime;

public class BookDTOFactory {

    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_TITLE = "Clean Code";
    public static final String DEFAULT_IMAGE_URL = "https://localhost:8080/uploads/image_url";
    public static final String DEFAULT_DESCRIPTION = "Descrição padrão do livro.";
    public static final Gender DEFAULT_GENDER = Gender.DESENVOLVIMENTO_SOFTWARE;
    public static final Boolean DEFAULT_ENABLED = false;
    public static final Boolean DEFAULT_AVAILABLE = true;
    public static final Long DEFAULT_USER_DTO = UserFactory.savedUser().getId();

    private BookDTOFactory() {}

    public static BookDTO savedBookDto(Long id, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return new BookDTO(
                id,
                DEFAULT_TITLE,
                DEFAULT_IMAGE_URL,
                userId,
                DEFAULT_DESCRIPTION,
                DEFAULT_GENDER,
                DEFAULT_ENABLED,
                DEFAULT_AVAILABLE,
                now,
                now
        );
    }

    public static BookDTO savedBookDto() {
        return savedBookDto(DEFAULT_ID, DEFAULT_USER_DTO);
    }
}
