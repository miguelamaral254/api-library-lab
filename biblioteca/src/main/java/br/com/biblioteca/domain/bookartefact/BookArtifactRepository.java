package br.com.biblioteca.domain.bookartefact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookArtifactRepository extends JpaRepository<BookArtifact, Long> , JpaSpecificationExecutor<BookArtifact> {
}
