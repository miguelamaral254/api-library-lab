package br.com.biblioteca.domain.authentication;

import br.com.biblioteca.core.BusinessException;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import br.com.biblioteca.domain.user.enums.UserExceptionCodeEnum;
import br.com.biblioteca.infrastructure.security.JwtConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @Transactional
    public User authenticateUser(String email, String password) {
        if (email == null || password == null) {
            throw new BusinessException(UserExceptionCodeEnum.EMAIL_AND_PASSWORD_DOES_NOT_MATCH);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserExceptionCodeEnum.EMAIL_AND_PASSWORD_DOES_NOT_MATCH));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(UserExceptionCodeEnum.EMAIL_AND_PASSWORD_DOES_NOT_MATCH);
        }

        return user;
    }

    public String generateToken(User user) {
        return jwtConfig.generateToken(user.getEmail(), user.getRole().name());
    }
}
