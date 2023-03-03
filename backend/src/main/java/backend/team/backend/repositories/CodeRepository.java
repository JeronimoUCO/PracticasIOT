package backend.team.backend.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.team.backend.models.CodeModel;

@Repository
public interface CodeRepository extends JpaRepository<CodeModel, String> {
    Optional<CodeModel> findById(String code);
}
