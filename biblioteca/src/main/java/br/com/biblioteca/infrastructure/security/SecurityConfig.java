package br.com.biblioteca.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(SecurityFilter securityFilter, CorsConfigurationSource corsConfigurationSource) {
        this.securityFilter = securityFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(RoutesConfig.PUBLIC_ROUTES).permitAll()
                        .requestMatchers(RoutesConfig.USER_ROUTES).hasAnyRole("PROFESSOR", "STUDENT","PARTNER_COMPANY", "ADMIN", "MANAGER")
                        .requestMatchers(RoutesConfig.COMPANY_ROUTES).hasAnyRole("PARTNER_COMPANY" ,"MANAGER","ADMIN")
                        .requestMatchers(RoutesConfig.MANAGERS_ROUTES).hasAnyRole("MANAGER","ADMIN")
                        .requestMatchers(RoutesConfig.ADMIN_ROUTES).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
