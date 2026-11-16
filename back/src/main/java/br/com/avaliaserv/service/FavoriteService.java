package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.ProfessionalDtos.ProfileResponse;
import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.Favorite;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.repository.FavoriteRepository;
import br.com.avaliaserv.repository.ProfessionalProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProfessionalProfileRepository profileRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<ProfileResponse> list() {
        return favoriteRepository.findByUserId(currentUser.get().getId()).stream()
                .map(Favorite::getProfileId)
                .map(profileRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(opt -> ProfileResponse.from(opt.get()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> ids() {
        return favoriteRepository.findByUserId(currentUser.get().getId()).stream()
                .map(Favorite::getProfileId).toList();
    }

    /** Alterna o favorito e devolve true se ficou favoritado. */
    @Transactional
    public boolean toggle(Long profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw ApiException.notFound("Profissional nao encontrado.");
        }
        User user = currentUser.get();
        var existing = favoriteRepository.findByUserIdAndProfileId(user.getId(), profileId);

        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return false;
        }
        favoriteRepository.save(Favorite.builder().user(user).profileId(profileId).build());
        return true;
    }
}
