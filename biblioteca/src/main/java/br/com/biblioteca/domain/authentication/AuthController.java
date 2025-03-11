package br.com.biblioteca.domain.authentication;

import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService jwtService;
    private final AuthMapper authMapper;


    @PostMapping("/login")
    public ResponseEntity<AuthDTO> login(@RequestBody AuthRequest authRequest) {
        User user = userService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        AuthDTO response = authMapper.toAuthDTO(user);
        response = new AuthDTO(response.idUser(), token, response.email(), response.role(), "Login successful");

        return ResponseEntity.ok(response);
    }
}