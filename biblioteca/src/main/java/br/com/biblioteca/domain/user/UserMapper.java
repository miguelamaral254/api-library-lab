
package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseMapper;
import br.com.biblioteca.domain.phone.PhoneMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PhoneMapper.class})
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @AfterMapping
    default void afterToEntity(UserDTO userDto, @MappingTarget User user) {
        if (user.getPhones() != null) {
            user.getPhones().forEach(phone -> phone.setUser(user));
        }
    }
}
