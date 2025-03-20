package br.com.biblioteca.domain.bookartefact;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.book.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_book_artifacts")
public class BookArtifact extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book bookId;

    @NotBlank
    private String description;
}