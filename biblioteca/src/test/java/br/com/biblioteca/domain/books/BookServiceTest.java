package br.com.biblioteca.domain.books;

import br.com.biblioteca.core.BaseException;
import br.com.biblioteca.domain.book.*;
import br.com.biblioteca.domain.books.factories.BookFactory;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.image.ImageStorageService;
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
import static br.com.biblioteca.domain.user.factories.UserFactory.savedUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookService bookService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ImageStorageService imageStorageService;

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
        var user = savedUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(imageStorageService.saveImage(file, request)).thenReturn("https://localhost:8080/uploads/image_url");

        book.setUserId(user);

        Book result = bookService.createBook(book, file, request);

        verify(bookRepository).save(book);
        verify(imageStorageService).saveImage(file, request);
        verify(userRepository).findById(userId);

        assertNotNull(result.getId());
        assertEquals("https://localhost:8080/uploads/image_url", result.getUrlImage());
    }
    @Test
    @DisplayName("Should throw exception when book is not found")
    void findById_whenBookNotFound_thenThrowException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        BaseException exception = assertThrows(BaseException.class, () -> bookService.findById(bookId));

        verify(bookRepository, times(1)).findById(bookId);

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should return book when book exists")
    void findById_whenBookExists_thenReturnBook() {
        Long bookId = 1L;
        Book book = BookFactory.savedBook(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.findById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
    }

    @Test
    @DisplayName("Should update Book successfully with Consumer")
    void updateBook_whenDataIsValid_thenUpdateSuccessfully() {
        Long bookId = 1L;
        Long userId = 42L;
        Book existingBook = BookFactory.savedBook(bookId);
        User existingUser = savedUser(userId);

        existingBook.setUserId(existingUser);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateBook(bookId, book -> book.setTitle("Novo Título"));

        verify(bookRepository, times(1)).findById(bookId);
        verify(userRepository, times(1)).findById(userId);
        verify(bookRepository, times(1)).save(any(Book.class));

        assertEquals("Novo Título", result.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when book is not found")
    void updateBook_whenBookNotFound_thenThrowException() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        BaseException exception = assertThrows(BaseException.class, () -> bookService.updateBook(bookId, book -> book.setTitle("Novo Título")));

        verify(bookRepository, times(1)).findById(bookId);

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void updateBook_whenUserNotFound_thenThrowException() {
        Long bookId = 1L;
        User existingUser = savedUser(bookId);

        Book existingBook = BookFactory.savedBook(bookId);
        existingBook.setUserId(existingUser);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.empty());
        BaseException exception = assertThrows(
                BaseException.class,
                () -> bookService.updateBook(bookId, book -> book.setTitle("Novo Título"))
        );

        assertEquals("User not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(userRepository, times(1)).findById(existingUser.getId());
    }

    @Test
    @DisplayName("Should update book available")
    void updateBook_whenBookAvailable_thenUpdateSuccessfully() {
        Long bookId = 1L;
        Book existingBook = BookFactory.savedBook(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.updateAvailability(bookId, false);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));

        assertNotNull(updatedBook);
        assertFalse(updatedBook.getAvailable());
    }
    @Test
    @DisplayName("Should delete book")
    void deleteBook_whenBookExists_thenDeleteSuccessfully() {
        Long bookId = 1L;
        Book existingBook = BookFactory.savedBook(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(existingBook);

    }

}
