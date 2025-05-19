package br.com.biblioteca.domain.auth;

import br.com.biblioteca.core.BaseException;
import br.com.biblioteca.domain.authentication.AuthDTO;
import br.com.biblioteca.domain.authentication.AuthService;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.factories.UserFactory;
import br.com.biblioteca.infrastructure.security.JwtConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Should authenticate user successfully with valid credentials")
    void authenticateUser_whenValidCredentials_thenReturnAuthDTO() {
        String email = "test@example.com";
        String password = "validPassword";
        String encodedPassword = "encodedPassword123";

        User user = UserFactory.savedUser(1L);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        String generatedToken = "generatedToken123";
        when(jwtConfig.generateToken(user.getEmail(), user.getRole().name())).thenReturn(generatedToken);

        User authenticatedUser = authService.authenticateUser(email, password);
        String token = authService.generateToken(authenticatedUser);

        AuthDTO authDTO = new AuthDTO(token, "Login successful");

        assertNotNull(authDTO);
        assertEquals(generatedToken, authDTO.token());
        assertEquals("Login successful", authDTO.message());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    @DisplayName("Should throw EMAIL_AND_PASSWORD_DOES_NOT_MATCH when email is invalid")
    void authenticateUser_whenInvalidEmail_thenThrowException() {
        String email = "invalid@example.com";
        String password = "anyPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        BaseException exception = assertThrows(BaseException.class,
                () -> authService.authenticateUser(email, password));

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("Should throw INVALID_PASSWORD when password is incorrect")
    void authenticateUser_whenInvalidPassword_thenThrowException() {
        String email = "test@example.com";
        String password = "invalidPassword";
        String encodedPassword = "encodedPassword123";

        User user = UserFactory.savedUser(1L);
        user.setPassword(encodedPassword);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        BaseException exception = assertThrows(BaseException.class,
                () -> authService.authenticateUser(email, password));


    assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    @DisplayName("Should throw EMAIL_AND_PASSWORD_DOES_NOT_MATCH when email is null")
    void authenticateUser_whenEmailIsNull_thenThrowException() {
        String password = "anyPassword";

        BaseException exception = assertThrows(BaseException.class,
                () -> authService.authenticateUser(null, password));


        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("Should throw EMAIL_AND_PASSWORD_DOES_NOT_MATCH when password is null")
    void authenticateUser_whenPasswordIsNull_thenThrowException() {
        String email = "test@example.com";

        BaseException exception = assertThrows(BaseException.class,
                () -> authService.authenticateUser(email, null));

        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).matches(any(), any());
    }

}
