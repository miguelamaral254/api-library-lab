package br.com.biblioteca.domain.user.factories;

import br.com.biblioteca.domain.user.UserDTO;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import java.time.LocalDateTime;

public class UserDTOFactory {

    public static final String DEFAULT_NAME = "John Doe";
    public static final String DEFAULT_EMAIL = "john.doe@example.com";
    public static final String DEFAULT_CPF = "12345678901";
    public static final String DEFAULT_PASSWORD = "password123";
    public static final String DEFAULT_NUMBER = "81996379353";
    public static final String DEFAULT_COUNTRY_CODE = "+55";
    public static final Role DEFAULT_ROLE = Role.USER;
    public static final Course DEFAULT_COURSE = Course.ADS;
    public static final Institution DEFAULT_INSTITUTION = Institution.SENAC;
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_IMAGE_URL = "image_url";

    private UserDTOFactory() {}

    public static UserDTO savedUserDto(Long id, String name, String email) {
        LocalDateTime now = LocalDateTime.now();
        return new UserDTO(
                id,
                DEFAULT_IMAGE_URL,
                name,
                DEFAULT_ROLE,
                DEFAULT_NUMBER,
                DEFAULT_COUNTRY_CODE,
                email,
                DEFAULT_PASSWORD,
                DEFAULT_CPF,
                DEFAULT_INSTITUTION,
                DEFAULT_COURSE,
                false,
                now,
                now
        );
    }

    public static UserDTO savedUserDto() {
        return savedUserDto(DEFAULT_ID, DEFAULT_NAME, DEFAULT_EMAIL);
    }
}