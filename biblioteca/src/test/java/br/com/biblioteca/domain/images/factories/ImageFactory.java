package br.com.biblioteca.domain.images.factories;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.*;

public class ImageFactory {

    public static final String ORIGINAL_FILE_NAME = "image.png";
    public static final byte[] FILE_CONTENT = "image content".getBytes();
    public static final String GENERATED_FILE_NAME = "uuid-image.png";
    public static final String IMAGE_URL = "http://localhost/uploads/" + GENERATED_FILE_NAME;

    public static MultipartFile mockMultipartFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(ORIGINAL_FILE_NAME);
        when(file.getBytes()).thenReturn(FILE_CONTENT);
        return file;
    }

    public static HttpServletRequest mockHttpRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getScheme()).thenReturn("http");
        when(request.getServerName()).thenReturn("localhost");
        when(request.getServerPort()).thenReturn(80);
        return request;
    }
}