package br.com.biblioteca.domain.authentication;

import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.infrastructure.security.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final AuthMapper authMapper;

    @PostMapping()
    public ResponseEntity<AuthDTO> login(@RequestBody AuthRequest authRequest) {
        User user = authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());

        String token = jwtConfig.generateToken(user.getEmail(), user.getRole().name());

        AuthDTO response = authMapper.toAuthDTO(user);
        response = new AuthDTO(response.idUser(), token, response.email(), response.role(), "Login successful");

        return ResponseEntity.ok(response);
    }
}