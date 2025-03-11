package br.com.biblioteca.domain.phone;

import br.com.biblioteca.core.BaseMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PhoneMapper extends BaseMapper<Phone, PhoneDTO> {

    @AfterMapping
    default void afterToEntity(PhoneDTO phoneDto, @MappingTarget Phone phone) {
        if (phone.getUser() != null) {
            phone.setUser(phone.getUser());
        }
    }
}