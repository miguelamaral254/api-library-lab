package br.com.biblioteca.domain.user;

import br.com.biblioteca.domain.user.factories.UserDTOFactory;
import br.com.biblioteca.domain.user.factories.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    UserMapper userMapper = new UserMapperImpl();

    @Test
    @DisplayName("Should map User entity to UserDTO successfully")
    void toDto_whenEntityProvided_thenReturnDto() {
        User user = UserFactory.savedUser();
        UserDTO userDto = userMapper.toDto(user);

        assertUserEqualsUserDto(user, userDto);
    }

    @Test
    @DisplayName("Should map list of User entities to list of UserDTOs successfully")
    void toDtoList_whenEntityListProvided_thenReturnDtoList() {
        List<User> userList = List.of(UserFactory.savedUser());
        List<UserDTO> userDtoList = userMapper.toDto(userList);

        assertNotNull(userDtoList);
        assertEquals(userList.size(), userDtoList.size());
        assertUserEqualsUserDto(userList.get(0), userDtoList.get(0));
    }

    @Test
    @DisplayName("Should map User Page to UserDTO Page successfully")
    void toDtoPage_whenEntityPageProvided_thenReturnDtoPage() {
        Page<User> userPage = new PageImpl<>(List.of(UserFactory.savedUser()), Pageable.ofSize(1), 1L);
        Page<UserDTO> userDtoPage = userMapper.toDto(userPage);

        assertNotNull(userDtoPage);
        assertEquals(userPage.getSize(), userDtoPage.getSize());
        assertEquals(userPage.getTotalElements(), userDtoPage.getTotalElements());
        assertUserEqualsUserDto(userPage.getContent().get(0), userDtoPage.getContent().get(0));
    }

    private void assertUserEqualsUserDto(User user, UserDTO userDto) {
        assertEquals(user.getId(), userDto.id());
        assertEquals(user.getImageUrl(), userDto.imageUrl());
        assertEquals(user.getName(), userDto.name());
        assertEquals(user.getRole(), userDto.role());
        assertEquals(user.getNumber(), userDto.number());
        assertEquals(user.getCountryCode(), userDto.countryCode());
        assertEquals(user.getEmail(), userDto.email());
        assertEquals(user.getPassword(), userDto.password());
        assertEquals(user.getCpf(), userDto.cpf());
        assertEquals(user.getInstitution(), userDto.institution());
        assertEquals(user.getCourse(), userDto.course());
        assertEquals(user.getEnabled(), userDto.enabled());
        assertEquals(user.getCreatedDate(), userDto.createdDate());
        assertEquals(user.getLastModifiedDate(), userDto.lastModifiedDate());
    }

    @Test
    @DisplayName("Should map UserDTO to User entity successfully")
    void toEntity_whenDtoProvided_thenReturnEntity() {
        UserDTO userDto = UserDTOFactory.savedUserDto();
        User user = userMapper.toEntity(userDto);

        assertUserDtoEqualsUser(userDto, user);
    }

    @Test
    @DisplayName("Should map list of UserDTOs to list of User entities successfully")
    void toEntityList_whenDtoListProvided_thenReturnEntityList() {
        List<UserDTO> userDtoList = List.of(UserDTOFactory.savedUserDto());
        List<User> userList = userMapper.toEntity(userDtoList);

        assertNotNull(userList);
        assertEquals(userDtoList.size(), userList.size());
        assertUserDtoEqualsUser(userDtoList.get(0), userList.get(0));
    }

    @Test
    @DisplayName("Should map UserDTO Page to User entity Page successfully")
    void toEntityPage_whenDtoPageProvided_thenReturnEntityPage() {
        Page<UserDTO> userDtoPage = new PageImpl<>(List.of(UserDTOFactory.savedUserDto()), Pageable.ofSize(1), 1L);
        Page<User> userPage = userMapper.toEntity(userDtoPage);

        assertNotNull(userPage);
        assertEquals(userDtoPage.getSize(), userPage.getSize());
        assertEquals(userDtoPage.getTotalElements(), userPage.getTotalElements());
        assertUserDtoEqualsUser(userDtoPage.getContent().get(0), userPage.getContent().get(0));
    }

    private void assertUserDtoEqualsUser(UserDTO userDto, User user) {
        assertEquals(userDto.id(), user.getId());
        assertEquals(userDto.imageUrl(), user.getImageUrl());
        assertEquals(userDto.name(), user.getName());
        assertEquals(userDto.role(), user.getRole());
        assertEquals(userDto.number(), user.getNumber());
        assertEquals(userDto.countryCode(), user.getCountryCode());
        assertEquals(userDto.email(), user.getEmail());
        assertEquals(userDto.password(), user.getPassword());
        assertEquals(userDto.cpf(), user.getCpf());
        assertEquals(userDto.institution(), user.getInstitution());
        assertEquals(userDto.course(), user.getCourse());
        assertEquals(userDto.enabled(), user.getEnabled());
        assertEquals(userDto.createdDate(), user.getCreatedDate());
        assertEquals(userDto.lastModifiedDate(), user.getLastModifiedDate());
    }

    @Test
    @DisplayName("Should merge UserDTO into User entity successfully")
    void mergeNonNull_whenDtoProvided_thenMergeDtoIntoEntity() {
        User user = UserFactory.savedUser(1L, "John Original", "original@email.com");
        UserDTO userDto = UserDTOFactory.savedUserDto(1L, "John Updated", "updated@email.com");

        assertDoesNotThrow(() -> userMapper.mergeNonNull(userDto, user));

        assertEquals(userDto.name(), user.getName());
        assertEquals(userDto.email(), user.getEmail());
    }
}