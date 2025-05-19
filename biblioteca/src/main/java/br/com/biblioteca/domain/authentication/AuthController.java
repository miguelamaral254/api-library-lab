package br.com.biblioteca.domain.authentication;

import br.com.biblioteca.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthMapper authMapper;

    @PostMapping()
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {

        AuthRequest request = authMapper.toAuthRequest(authRequestDTO);
        User authenticatedUser = authService.authenticateUser(request.getEmail(), request.getPassword());
        String token = authService.generateToken(authenticatedUser);
        AuthDTO authDTO = new AuthDTO(token, "Login successful");
        AuthResponseDTO response = authMapper.toAuthResponseDTO(authDTO);

        return ResponseEntity.ok(response);
    }
}
