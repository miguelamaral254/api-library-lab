package br.com.biblioteca.domain.phone;

import br.com.biblioteca.core.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper extends BaseMapper<Phone, PhoneDTO> {
}