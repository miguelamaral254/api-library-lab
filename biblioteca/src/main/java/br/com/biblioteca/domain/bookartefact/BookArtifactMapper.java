package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.domain.book.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import br.com.biblioteca.core.BaseMapper;

@Mapper(componentModel = "spring")
public interface BookArtifactMapper extends BaseMapper<BookArtifact, BookArtifactDTO> {

    @Mapping(source = "bookId", target = "bookId")
    BookArtifactDTO toDTO(BookArtifact bookArtifact);

    @Override
    @Mapping(source = "bookId", target = "bookId")
    BookArtifact toEntity(BookArtifactDTO bookArtifactDTO);

    default Long map(Book book) {
        if (book == null) {
            return null;
        }
        return book.getId();
    }

    default Book map(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}