package br.com.biblioteca.domain.image;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ImageUrlBuilder {

    private static final String PROD_DOMAIN = "porto-library.com.br";
    private static final String PROD_URL = "https://porto-library.com.br/appevento/uploads/";

    public String buildUrl(HttpServletRequest request, String fileName) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");

        if (baseUrl.contains(PROD_DOMAIN)) {
            return PROD_URL + fileName;
        }

        return baseUrl + "/uploads/" + fileName;
    }
}