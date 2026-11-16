package br.com.avaliaserv.repository;

import br.com.avaliaserv.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findFirstByEmailAndUsedFalseOrderByIdDesc(String email);
}
