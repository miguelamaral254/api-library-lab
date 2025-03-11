package br.com.biblioteca.domain.authentication;

import br.com.biblioteca.core.BaseMapper;
import br.com.biblioteca.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper extends BaseMapper {

    @Mapping(source = "id", target = "idUser")
    AuthDTO toAuthDTO(User user);
}