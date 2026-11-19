package br.com.avaliaserv.repository;

import br.com.avaliaserv.model.ProfessionalProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile, Long> {
    Optional<ProfessionalProfile> findByUserId(Long userId);
    Optional<ProfessionalProfile> findByUserEmailIgnoreCase(String email);
    boolean existsByDemoTrue();
}
