package br.com.biblioteca.domain.user;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.user.enums.Course;
import br.com.biblioteca.domain.user.enums.Institution;
import br.com.biblioteca.domain.user.enums.Role;
import br.com.biblioteca.domain.user.enums.UserExceptionCodeEnum;
import br.com.biblioteca.domain.phone.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(User user) {
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
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserExceptionCodeEnum.USER_NOT_FOUND));

        validateUpdateRules(existingUser.getName(), updatedUser);
        updateUserFields(existingUser, updatedUser);
        managePhones(existingUser, updatedUser);

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Transactional
    public User disableUser(Long id, Boolean disable) {
        User user = findById(id);
        user.setEnabled(disable);
        return userRepository.save(user);
    }

    @Transactional
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserExceptionCodeEnum.EMAIL_DOES_NOT_MATCH));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_PASSWORD);
        }
        return user;
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

        if (user.getPhones() == null || user.getPhones().isEmpty()) {
            throw new BusinessException(UserExceptionCodeEnum.INVALID_PHONE);
        }
    }
    private void validateUpdateRules(String oldUserName, User updatedUser) {
        validateBusinessRules(updatedUser);
        if (!oldUserName.equals(updatedUser.getName()) &&
                userRepository.existsByNameAndIdNot(updatedUser.getName(), updatedUser.getId())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_USER);
        }
        if (!updatedUser.getEmail().equals(updatedUser.getEmail()) &&
                userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_EMAIL);
        }
        if (updatedUser.getCpf() != null && !updatedUser.getCpf().equals(updatedUser.getCpf()) &&
                userRepository.existsByCpf(updatedUser.getCpf())) {
            throw new BusinessException(UserExceptionCodeEnum.DUPLICATE_CPF);
        }
    }

    private void updateUserFields(User existingUser, User updatedUser) {
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getEnabled() != null) {
            existingUser.setEnabled(updatedUser.getEnabled());
        }
        if (updatedUser.getCpf() != null) {
            existingUser.setCpf(updatedUser.getCpf());
        }
    }

    private void managePhones(User existingUser, User updatedUser) {
        if (updatedUser.getPhones() != null) {
            for (Phone phone : updatedUser.getPhones()) {
                if (phone.getId() == null) {
                    phone.setUser(existingUser);
                    existingUser.getPhones().add(phone);
                } else {
                    for (Phone existingPhone : existingUser.getPhones()) {
                        if (existingPhone.getId().equals(phone.getId())) {
                            existingPhone.setNumber(phone.getNumber());
                            existingPhone.setCountryCode(phone.getCountryCode());
                        }
                    }
                }
            }
        }
    }
}