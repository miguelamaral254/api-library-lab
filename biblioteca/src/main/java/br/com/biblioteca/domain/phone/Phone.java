package br.com.biblioteca.domain.phone;

import br.com.biblioteca.core.BaseEntity;
import br.com.biblioteca.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_phones")
@NoArgsConstructor
@AllArgsConstructor
public class Phone extends BaseEntity {

    @Column(name = "number", length = 45, nullable = false)
    private String number;

    @Column(name = "country_code", length = 5, nullable = false)
    private String countryCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}