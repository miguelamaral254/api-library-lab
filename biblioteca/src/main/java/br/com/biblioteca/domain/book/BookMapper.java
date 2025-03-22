package br.com.biblioteca.domain.book;

import br.com.biblioteca.domain.user.User;
import org.mapstruct.*;
import br.com.biblioteca.core.BaseMapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookDTO> {

    @Mapping(source = "userId", target = "userId")
    BookDTO toDTO(Book book);

    @Override
    @Mapping(source = "userId", target = "userId")
    Book toEntity(BookDTO bookDTO);

    @Override
    @Mapping(source = "userId", target = "userId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeNonNull(BookDTO dto, @MappingTarget Book entity);

    default Long map(User user) {
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    default User map(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}