package br.com.biblioteca.domain.bookrent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRentRepository extends JpaRepository<BookRent, Long> , JpaSpecificationExecutor<BookRent> { }
