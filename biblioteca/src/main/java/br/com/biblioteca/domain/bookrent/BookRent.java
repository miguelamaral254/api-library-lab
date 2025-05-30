package br.com.biblioteca.domain.bookrent;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.book.Book;
import br.com.biblioteca.domain.user.User;
import br.com.biblioteca.domain.user.enums.Institution;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_book_rent")
@AllArgsConstructor

@NoArgsConstructor

public class BookRent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book bookId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(nullable = true)
    private LocalDateTime devolucao;

    @Column(nullable = false)
    private boolean atrasado;
}
