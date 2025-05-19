package br.com.biblioteca.domain.authentication;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthRequest toAuthRequest(AuthRequestDTO authRequestDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "message", target = "message")
    AuthResponseDTO toAuthResponseDTO(AuthDTO authDTO);
}
