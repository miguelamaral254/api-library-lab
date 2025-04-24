package br.com.biblioteca.domain.user.factories;

import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserFactory {

    public static final String DEFAULT_NAME = "John Doe";
    public static final String DEFAULT_EMAIL = "john.doe@example.com";
    public static final String DEFAULT_CPF = "12345678901";
    public static final String DEFAULT_PASSWORD = "encodedPassword123";
    public static final String DEFAULT_NUMBER = "81996379353";
    public static final String DEFAULT_COUNTRY_CODE = "+55";
    public static final Role DEFAULT_ROLE = Role.USER;
    public static final Course DEFAULT_COURSE = Course.ADS;
    public static final Institution DEFAULT_INSTITUTION = Institution.SENAC;
    public static final Long DEFAULT_ID = 1L;
    public static final String DEFAULT_IMAGE_URL = "https://localhost:8080/uploads/image_url";
    public static final Boolean DEFAULT_ENABLED = false;


    private UserFactory() {}

    private static User baseUser() {
        User user = new User();
        user.setName(DEFAULT_NAME);
        user.setImageUrl(DEFAULT_IMAGE_URL);
        user.setCpf(DEFAULT_CPF);
        user.setEmail(DEFAULT_EMAIL);
        user.setRole(DEFAULT_ROLE);
        user.setCourse(DEFAULT_COURSE);
        user.setInstitution(DEFAULT_INSTITUTION);
        user.setPassword(DEFAULT_PASSWORD);
        user.setNumber(DEFAULT_NUMBER);
        user.setCountryCode(DEFAULT_COUNTRY_CODE);
        user.setEnabled(DEFAULT_ENABLED);

        return user;
    }

    public static User validUser() {
        return baseUser();
    }

    public static User savedUser(Long id, String name, String email) {
        User user = baseUser();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedDate(now);
        user.setLastModifiedDate(now);
        return user;
    }

    public static User savedUser(Long id, String name) {
        return savedUser(id, name, DEFAULT_EMAIL);
    }

    public static User savedUser(Long id) {
        return savedUser(id, DEFAULT_NAME);
    }

    public static User savedUser() {
        return savedUser(DEFAULT_ID);
    }

    public static HttpServletRequest createHttpServletRequestMock() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getScheme()).thenReturn("https");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(8080);
        return request;
    }

    public static User userWithCustomValues(String name, String email) {
        User user = baseUser();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}