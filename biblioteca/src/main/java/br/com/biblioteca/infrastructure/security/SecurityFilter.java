package br.com.biblioteca.infrastructure.security;

import br.com.biblioteca.domain.exceptions.NotFoundException;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null && jwtConfig.validateToken(token)) {
            String email = jwtConfig.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found"));

            var authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.replace("Bearer ", "") : null;
    }
}