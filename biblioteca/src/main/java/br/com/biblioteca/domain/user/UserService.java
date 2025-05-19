package br.com.biblioteca.domain.user;

import br.com.biblioteca.domain.exceptions.ConflictException;
import br.com.biblioteca.domain.exceptions.InvalidException;
import br.com.biblioteca.domain.exceptions.NotFoundException;
import br.com.biblioteca.domain.exceptions.ServerException;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import br.com.biblioteca.infrastructure.conf.ImageConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageConfig imageConfig;

    @Transactional
    public User createUser(User user, MultipartFile file, HttpServletRequest request) {
        validateImageCreateRules(user, file, request);
        validateBusinessRules(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Page<User> searchUsers(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    @Cacheable(value = "users", key = "#id")
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional
    public User updateUser(Long id, Consumer<User> mergeNonNull) {
        User user = findById(id);
        final String oldEmail = user.getEmail();
        final String oldCpf = user.getCpf();
        mergeNonNull.accept(user);
        validateUpdate(user, oldEmail, oldCpf);

        return userRepository.save(user);
    }

    @Transactional
    public User disableUser(Long id, Boolean disable) {
        User user = findById(id);
        user.setEnabled(disable);
        return userRepository.save(user);
    }

    private void validateBusinessRules(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        if (user.getCpf() != null && userRepository.existsByCpf(user.getCpf())) {
            throw new ConflictException("Cpf already exists");
        }

        if (user.getEmail() == null || !user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zAZ0-9.-]+\\.[a-zA-Z]{2,6}")) {
            throw new InvalidException("Email are with Invalid format");
        }

        if (user.getCpf() != null && !user.getCpf().matches("\\d{11}")) {
            throw new InvalidException("Cpf are with Invalid format");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new InvalidException("Password are with Invalid format");
        }

        if (user.getName() == null || user.getName().trim().isEmpty() ||
                !user.getName().matches("[A-Za-zÀ-ÿ\\s'-]+")) {
            throw new InvalidException("Name are with Invalid format");
        }

        if (user.getInstitution() == null || !Arrays.asList(Institution.values()).contains(user.getInstitution())) {
            throw new NotFoundException("Institution does not exist");
        }

        if (user.getCourse() == null || !Arrays.asList(Course.values()).contains(user.getCourse())) {
            throw new NotFoundException("Course does not exist");
        }

        if (user.getRole() == null || !Arrays.asList(Role.values()).contains(user.getRole())) {
            throw new NotFoundException("Role does not exist");
        }

    }

    private void validateImageCreateRules(User user, MultipartFile file, HttpServletRequest request) {
        try {
            if (file != null && !file.isEmpty()) {
                String urlImage = imageConfig.saveImage(file, request);
                user.setImageUrl(urlImage);
            }

        } catch (IOException e) {
            throw new ServerException("Server error");
        }
    }

    private void validateUpdate(User user, String oldEmail, String oldCpf) {
        if (!oldEmail.equals(user.getEmail()) && userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new ConflictException("Email already exists");
        }

        if (!oldCpf.equals(user.getCpf()) && userRepository.existsByCpfAndIdNot(user.getCpf(), user.getId())) {
            throw new ConflictException("CPF already exists");
        }
    }
}