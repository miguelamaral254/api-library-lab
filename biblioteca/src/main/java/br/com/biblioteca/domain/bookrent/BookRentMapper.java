package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.core.BaseMapper;
import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookRentMapper extends BaseMapper<BookRent, BookRentDTO> {

    @Override
    @Mapping(source = "bookId", target = "bookId")
    @Mapping(source = "userId", target = "userId")
    BookRentDTO toDto(BookRent bookRent);

    @Override
    @Mapping(source = "bookId", target = "bookId")
    @Mapping(source = "userId", target = "userId")
    BookRent toEntity(BookRentDTO bookRentDto);

    default Long map(Book book) {
        return book != null ? book.getId() : null;
    }

    default Book map(Long id) {
        if (id == null) return null;
        Book book = new Book();
        book.setId(id);
        return book;
    }

    default Long map(User user) {
        return user != null ? user.getId() : null;
    }

    default User mapUser(Long id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
