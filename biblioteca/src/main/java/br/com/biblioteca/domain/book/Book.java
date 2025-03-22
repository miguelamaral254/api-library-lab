package br.com.biblioteca.domain.book;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_books")
public class Book extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;
    //TODO: IMPLEMENTAR FOTO DE CAPA
    //private String urlImage;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Boolean available;


    protected void onCreate() {
        super.onCreate();
        if (this.available == null) {
            this.available = true;
        }
    }
}