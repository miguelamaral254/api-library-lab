package br.com.biblioteca.domain.books.factories;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.Gender;
import br.com.biblioteca.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import static br.com.biblioteca.domain.user.factories.UserFactory.savedUser;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookFactory {

    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_TITLE = "Cleancode";
    public static final String DEFAULT_IMAGE_URL = "https://localhost:8080/uploads/image_url";
    public static final User DEFAULT_USER = savedUser()  ;
    public static final String DEFAULT_DESCRIPTION = "teste muito lorem";
    public static final Boolean DEFAULT_AVAILABLE = true;
    public static final Gender DEFAULT_GENDER = Gender.DESENVOLVIMENTO_SOFTWARE;
    public static final Boolean DEFAULT_ENABLED = false;


    private BookFactory() {}

    private static Book baseBook() {
        Book book = new Book();
        book.setTitle(DEFAULT_TITLE);
        book.setUrlImage(DEFAULT_IMAGE_URL);
        book.setUserId(DEFAULT_USER);
        book.setDescription(DEFAULT_DESCRIPTION);
        book.setGender(DEFAULT_GENDER);
        book.setAvailable(DEFAULT_AVAILABLE);
        book.setEnabled(DEFAULT_ENABLED);


        return book;
    }

    public static Book validBook() {
        return baseBook();
    }

    public static Book savedBook(Long id, String title, String description, Gender gender, Boolean enabled) {
        Book book = baseBook();
        book.setId(id);
        book.setTitle(title);
        book.setDescription(description);
        book.setGender(gender);
        book.setEnabled(enabled);
        LocalDateTime now = LocalDateTime.now();
        book.setCreatedDate(now);
        book.setLastModifiedDate(now);
        return book;
    }    public static Book savedBook(Long id, String title) {
        return savedBook(id, title, DEFAULT_DESCRIPTION, DEFAULT_GENDER, DEFAULT_ENABLED);
    }

    public static Book savedBook(Long id) {
        return savedBook(id, DEFAULT_TITLE);
    }

    public static Book savedBook() {
        return savedBook(DEFAULT_ID);
    }

    public static HttpServletRequest createHttpServletRequestMock() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getScheme()).thenReturn("https");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);
        return request;
    }

    public static Book bookWithCustomValues(String title, String description) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setGender(DEFAULT_GENDER);
        book.setEnabled(DEFAULT_ENABLED);
        book.setUrlImage(DEFAULT_IMAGE_URL);
        book.setUserId(DEFAULT_USER);
        return book;
    }
}
