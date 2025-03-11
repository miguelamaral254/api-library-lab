package br.com.biblioteca.core;

import java.time.LocalDateTime;

public interface BaseDTO {

    Long id();

    LocalDateTime createdDate();

    LocalDateTime lastModifiedDate();

}