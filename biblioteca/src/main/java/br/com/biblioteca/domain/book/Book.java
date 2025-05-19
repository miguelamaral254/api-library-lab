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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User userId;

    @NotBlank
    @Column(nullable = false)
    private String urlImage;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotBlank
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Boolean available;

    protected void onCreate() {
        super.onCreate();
        if (this.available == null) {
            this.available = true;
        }
    }
}