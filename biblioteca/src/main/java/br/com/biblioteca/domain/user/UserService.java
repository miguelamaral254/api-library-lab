package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.book.BookExceptionCodeEnum;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import br.com.biblioteca.domain.user.enums.UserExceptionCodeEnum;
import br.com.biblioteca.infrastructure.conf.ImageConf;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    private final ImageConf imageConf;

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

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserExceptionCodeEnum.USER_NOT_FOUND));
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
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_EMAIL);
        }

        if (user.getCpf() != null && userRepository.existsByCpf(user.getCpf())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_CPF);
        }

        if (user.getEmail() == null || !user.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zAZ0-9.-]+\\.[a-zA-Z]{2,6}")) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_EMAIL);
        }

        if (user.getCpf() != null && !user.getCpf().matches("\\d{11}")) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_CPF);
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_PASSWORD);
        }

        if (user.getName() == null || user.getName().trim().isEmpty() ||
                !user.getName().matches("[A-Za-zÀ-ÿ\\s'-]+")) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_NAME);
        }

        if (user.getInstitution() == null || !Arrays.asList(Institution.values()).contains(user.getInstitution())) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_INSTITUTION);
        }

        if (user.getCourse() == null || !Arrays.asList(Course.values()).contains(user.getCourse())) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_COURSE);
        }

        if (user.getRole() == null || !Arrays.asList(Role.values()).contains(user.getRole())) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_ROLE);
        }

    }

    private void validateImageCreateRules(User user, MultipartFile file, HttpServletRequest request) {
        try {


            if (file != null && !file.isEmpty()) {
                String urlImage = imageConf.saveImage(file, request);
                user.setImageUrl(urlImage);
            }

        } catch (IOException e) {
            throw new BusinessException(BookExceptionCodeEnum.IMAGE_CREATION_FAILED);
        }
    }

    private void validateUpdate(User user, String oldEmail, String oldCpf) {
        if (!oldEmail.equals(user.getEmail()) && userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_EMAIL);
        }

        if (!oldCpf.equals(user.getCpf()) && userRepository.existsByCpfAndIdNot(user.getCpf(), user.getId())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_USER);
        }
    }
}