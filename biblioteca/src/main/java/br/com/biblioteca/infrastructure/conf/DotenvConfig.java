package br.com.biblioteca.infrastructure.conf;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            // usar pra debugar .env
            boolean showInfo = false;
            if (showInfo) {
                System.out.println("DEBUG INFO - Variáveis principais carregadas:");
                System.out.println("DATABASE_URL: " + System.getProperty("DATABASE_URL"));
                System.out.println("DATABASE_USER: " + System.getProperty("DATABASE_USER"));
                System.out.println("DATABASE_PASSWORD: " + System.getProperty("DATABASE_PASSWORD"));
                System.out.println("CORS_ALLOWED_ORIGINS: " + System.getProperty("CORS_ALLOWED_ORIGINS"));
            }
        });


    }
}
