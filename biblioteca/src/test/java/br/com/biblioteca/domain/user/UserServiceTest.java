package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BaseException;
import br.com.biblioteca.domain.user.factories.UserFactory;
import br.com.biblioteca.domain.image.ImageStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ImageStorageService imageStorageService;

    @Mock
    private MultipartFile file;

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("Should create User successfully when all data is valid")
    void createUser_whenUserIsValid_thenCreateSuccessfully() throws IOException {
        User user = UserFactory.validUser();
        User savedUser = UserFactory.savedUser(1L);
        String encodedPassword = "encodedPassword123";

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByCpf(user.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(UserFactory.DEFAULT_PASSWORD)).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(imageStorageService.saveImage(file, request)).thenReturn("https://localhost:8080/uploads/image_url");

        User createdUser = userService.createUser(user, file, request);

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).existsByCpf(user.getCpf());
        verify(passwordEncoder, times(1)).encode(UserFactory.DEFAULT_PASSWORD);
        verify(userRepository, times(1)).save(user);
        verify(imageStorageService, times(1)).saveImage(file, request);

        assertNotNull(createdUser.getId());
        assertNotNull(createdUser.getCreatedDate());
        assertNotNull(createdUser.getLastModifiedDate());
        assertFalse(createdUser.getEnabled());
        assertEquals("https://localhost:8080/uploads/image_url", createdUser.getImageUrl());
        assertEquals(encodedPassword, createdUser.getPassword());
    }
    @Test
    @DisplayName("Should throw DUPLICATE_EMAIL when User email already exists")
    void createUser_whenEmailExists_thenThrowDuplicateEmail() {
        User user = UserFactory.validUser();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        BaseException exception = assertThrows(BaseException.class,
                () -> userService.createUser(user, file, request));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
    @Test
    @DisplayName("Should find User by ID successfully when user exists")
    void findById_whenUserExists_thenReturnUser() {
        Long userId = 1L;
        User expectedUser = UserFactory.savedUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User foundUser = userService.findById(userId);

        verify(userRepository, times(1)).findById(userId);
        assertEquals(expectedUser, foundUser);
    }

    @Test
    @DisplayName("Should throw USER_NOT_FOUND when User does not exist")
    void findById_whenUserNotExists_thenThrowUserNotFound() {
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        BaseException exception = assertThrows(BaseException.class,
                () -> userService.findById(userId));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should search Users successfully with specification")
    void searchUsers_whenCalled_thenReturnUsers() {
        Pageable pageable = Pageable.ofSize(10);
        Specification<User> spec = Specification.where(null);
        Page<User> expectedPage = new PageImpl<>(List.of(UserFactory.savedUser(1L)));

        when(userRepository.findAll(spec, pageable)).thenReturn(expectedPage);

        Page<User> resultPage = userService.searchUsers(spec, pageable);

        verify(userRepository, times(1)).findAll(spec, pageable);
        assertEquals(1, resultPage.getTotalElements());
    }

    @Test
    @DisplayName("Should update User successfully with Consumer")
    void updateUser_whenDataIsValid_thenUpdateSuccessfully() {
        Long userId = 1L;
        User existingUser = UserFactory.savedUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(userId, user -> user.setName("Novo Nome"));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("Novo Nome", result.getName());

        verify(userRepository, never()).existsByEmailAndIdNot(anyString(), anyLong());
        verify(userRepository, never()).existsByCpfAndIdNot(anyString(), anyLong());
    }
    @Test
    @DisplayName("Should throw DUPLICATE_EMAIL when updating to existing email")
    void updateUser_whenEmailExists_thenThrowDuplicateEmail() {
        Long userId = 1L;
        User existingUser = UserFactory.savedUser(userId);
        String newEmail = "existing@example.com";

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(newEmail, userId)).thenReturn(true);

        BaseException exception = assertThrows(BaseException.class,
                () -> userService.updateUser(userId, user -> user.setEmail(newEmail)));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
    @Test
    @DisplayName("Should throw DUPLICATE_USER when updating to existing CPF")
    void updateUser_whenCpfExists_thenThrowDuplicateUser() {
        Long userId = 1L;
        User existingUser = UserFactory.savedUser(userId);
        String newCpf = "98765432100";

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByCpfAndIdNot(newCpf, userId)).thenReturn(true);

        BaseException exception = assertThrows(BaseException.class,
                () -> userService.updateUser(userId, user -> user.setCpf(newCpf)));

        assertEquals("CPF already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should disable User successfully")
    void disableUser_whenCalled_thenDisableUser() {
        Long userId = 1L;
        User user = UserFactory.savedUser(userId);
        user.setEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User disabledUser = userService.disableUser(userId, false);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
        assertFalse(disabledUser.getEnabled());
    }

    @Test
    @DisplayName("Should update User without changing email/CPF successfully")
    void updateUser_whenEmailCpfNotChanged_thenUpdateSuccessfully() {
        Long userId = 1L;
        User existingUser = UserFactory.savedUser(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(userId, user -> user.setName("Novo Nome"));

        verify(userRepository, never()).existsByEmailAndIdNot(any(), any());
        verify(userRepository, never()).existsByCpfAndIdNot(any(), any());
        assertEquals("Novo Nome", result.getName());
    }
}