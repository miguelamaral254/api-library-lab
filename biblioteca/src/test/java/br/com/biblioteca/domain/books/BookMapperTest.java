package br.com.biblioteca.domain.books;

import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.book.BookDTO;
import br.com.biblioteca.domain.book.BookMapper;
import br.com.biblioteca.domain.book.BookMapperImpl;
import br.com.biblioteca.domain.books.factories.BookDTOFactory;
import br.com.biblioteca.domain.books.factories.BookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static br.com.biblioteca.domain.user.factories.UserFactory.savedUser;
import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    BookMapper bookMapper = new BookMapperImpl();

    @Test
    @DisplayName("Should map Book to BookDTO")
    void toDto_whenEntityProvided_thenReturnDto() {
        Book book = BookFactory.savedBook();
        BookDTO bookDto = bookMapper.toDto(book);

        assertBookEqualsBookDto(book, bookDto);
    }

    @Test
    @DisplayName("Should map list of Book to list of BookDTO")
    void toDtoList_whenEntityListProvided_thenReturnDtoList() {
        List<Book> books = List.of(BookFactory.savedBook());
        List<BookDTO> bookDTOs = bookMapper.toDto(books);

        assertNotNull(bookDTOs);
        assertEquals(books.size(), bookDTOs.size());
        assertBookEqualsBookDto(books.get(0), bookDTOs.get(0));
    }

    @Test
    @DisplayName("Should map Book Page to BookDTO Page")
    void toDtoPage_whenEntityPageProvided_thenReturnDtoPage() {
        Page<Book> bookPage = new PageImpl<>(List.of(BookFactory.savedBook()), Pageable.ofSize(1), 1L);
        Page<BookDTO> bookDtoPage = bookMapper.toDto(bookPage);

        assertNotNull(bookDtoPage);
        assertEquals(bookPage.getSize(), bookDtoPage.getSize());
        assertEquals(bookPage.getTotalElements(), bookDtoPage.getTotalElements());
        assertBookEqualsBookDto(bookPage.getContent().get(0), bookDtoPage.getContent().get(0));
    }

    @Test
    @DisplayName("Should map BookDTO to Book")
    void toEntity_whenDtoProvided_thenReturnEntity() {
        BookDTO bookDto = BookDTOFactory.savedBookDto();
        Book book = bookMapper.toEntity(bookDto);

        assertBookDtoEqualsBook(bookDto, book);
    }

    @Test
    @DisplayName("Should map list of BookDTO to list of Book")
    void toEntityList_whenDtoListProvided_thenReturnEntityList() {
        List<BookDTO> bookDTOs = List.of(BookDTOFactory.savedBookDto());
        List<Book> books = bookMapper.toEntity(bookDTOs);

        assertNotNull(books);
        assertEquals(bookDTOs.size(), books.size());
        assertBookDtoEqualsBook(bookDTOs.get(0), books.get(0));
    }

    @Test
    @DisplayName("Should map BookDTO Page to Book Page")
    void toEntityPage_whenDtoPageProvided_thenReturnEntityPage() {
        Page<BookDTO> bookDtoPage = new PageImpl<>(List.of(BookDTOFactory.savedBookDto()), Pageable.ofSize(1), 1L);
        Page<Book> bookPage = bookMapper.toEntity(bookDtoPage);

        assertNotNull(bookPage);
        assertEquals(bookDtoPage.getSize(), bookPage.getSize());
        assertEquals(bookDtoPage.getTotalElements(), bookPage.getTotalElements());
        assertBookDtoEqualsBook(bookDtoPage.getContent().get(0), bookPage.getContent().get(0));
    }

    @Test
    @DisplayName("Should merge BookDTO into Book")
    void mergeNonNull_whenDtoProvided_thenMergeDtoIntoEntity() {
        Book book = BookFactory.savedBook(1L, "Título Original");
        BookDTO bookDto = BookDTOFactory.savedBookDto(1L, savedUser().getId());

        assertDoesNotThrow(() -> bookMapper.mergeNonNull(bookDto, book));

        assertEquals(bookDto.title(), book.getTitle());
        assertEquals(bookDto.description(), book.getDescription());
    }

    private void assertBookEqualsBookDto(Book book, BookDTO dto) {
        assertEquals(book.getId(), dto.id());
        assertEquals(book.getTitle(), dto.title());
        assertEquals(book.getDescription(), dto.description());
        assertEquals(book.getGender(), dto.gender());
        assertEquals(book.getEnabled(), dto.enabled());
        assertEquals(book.getUrlImage(), dto.urlImage());
        assertEquals(book.getUserId().getId(), dto.userId());
        assertEquals(book.getAvailable(), dto.available());
        assertEquals(book.getCreatedDate(), dto.createdDate());
        assertEquals(book.getLastModifiedDate(), dto.lastModifiedDate());
    }

    private void assertBookDtoEqualsBook(BookDTO dto, Book book) {
        assertEquals(dto.id(), book.getId());
        assertEquals(dto.title(), book.getTitle());
        assertEquals(dto.description(), book.getDescription());
        assertEquals(dto.gender(), book.getGender());
        assertEquals(dto.enabled(), book.getEnabled());
        assertEquals(dto.urlImage(), book.getUrlImage());
        assertEquals(dto.userId(), book.getUserId().getId());
        assertEquals(dto.available(), book.getAvailable());
        assertEquals(dto.createdDate(), book.getCreatedDate());
        assertEquals(dto.lastModifiedDate(), book.getLastModifiedDate());
    }
}
