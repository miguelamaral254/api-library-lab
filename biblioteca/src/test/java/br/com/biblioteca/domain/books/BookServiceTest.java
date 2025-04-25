package br.com.biblioteca.domain.books;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookRepository;
import br.com.biblioteca.domain.book.BookService;
import br.com.biblioteca.domain.books.factories.BookFactory;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.factories.UserFactory;
import br.com.biblioteca.infrastructure.conf.ImageConf;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookService bookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageConf imageConf;

    @Mock
    private MultipartFile file;

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("Should create Book successfully when data is valid")
    void createBook_whenBookIsValid_thenCreateSuccessfully() throws IOException {
        Book book = BookFactory.validBook();
        Book savedBook = BookFactory.savedBook(1L);

        Long userId = 42L;
        var user = UserFactory.savedUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(imageConf.saveImage(file, request)).thenReturn("https://localhost:8080/uploads/image_url");

        book.setUserId(user);

        Book result = bookService.createBook(book, file, request);

        verify(bookRepository).save(book);
        verify(imageConf).saveImage(file, request);
        verify(userRepository).findById(userId);
        assertNotNull(result.getId());
        assertEquals("https://localhost:8080/uploads/image_url", result.getUrlImage());
    }

}
