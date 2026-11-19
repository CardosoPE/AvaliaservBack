package br.com.avaliaserv.repository;

import br.com.avaliaserv.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndProfileId(Long userId, Long profileId);
    void deleteByUserId(Long userId);
}
