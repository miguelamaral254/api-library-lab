package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserDTO> { }