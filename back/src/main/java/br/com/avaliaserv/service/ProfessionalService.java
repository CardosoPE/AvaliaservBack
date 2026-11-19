package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.ProfessionalDtos.ProfileResponse;
import br.com.avaliaserv.dto.ProfessionalDtos.ReviewRequest;
import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.ProfessionalProfile;
import br.com.avaliaserv.model.Review;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.repository.ProfessionalProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalProfileRepository profileRepository;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public List<ProfileResponse> search(String q, String category, String city, String availability) {
        String term = q == null ? "" : q.trim().toLowerCase(Locale.ROOT);

        return profileRepository.findAll().stream()
                .filter(p -> term.isEmpty() || matches(p, term))
                .filter(p -> isBlank(category) || category.equalsIgnoreCase(p.getCategory()))
                .filter(p -> isBlank(city) || (p.getLocation() != null
                        && p.getLocation().toLowerCase(Locale.ROOT).contains(city.toLowerCase(Locale.ROOT))))
                .filter(p -> isBlank(availability) || availability.equalsIgnoreCase(p.getAvailability()))
                .sorted(Comparator.comparing((ProfessionalProfile p) ->
                        "online".equalsIgnoreCase(p.getStatus()) ? 0 : 1))
                .map(ProfileResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfileResponse byId(Long id) {
        return ProfileResponse.from(find(id));
    }

    @Transactional
    public ProfileResponse addReview(Long profileId, ReviewRequest req) {
        if (req.rating() < 1 || req.rating() > 5) throw ApiException.badRequest("A nota deve ficar entre 1 e 5.");

        ProfessionalProfile profile = find(profileId);
        User author = currentUser.get();

        if (author.equals(profile.getUser())) {
            throw ApiException.badRequest("Voce nao pode avaliar o proprio perfil.");
        }

        boolean alreadyReviewed = profile.getReviews().stream()
                .anyMatch(r -> author.getEmail().equalsIgnoreCase(r.getAuthorEmail()));
        if (alreadyReviewed) throw ApiException.badRequest("Voce ja avaliou este profissional.");

        profile.getReviews().add(Review.builder()
                .profile(profile)
                .authorName(author.getName())
                .authorEmail(author.getEmail())
                .text(req.text())
                .rating(req.rating())
                .build());

        return ProfileResponse.from(profileRepository.save(profile));
    }

    private ProfessionalProfile find(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Profissional nao encontrado."));
    }

    private boolean matches(ProfessionalProfile p, String term) {
        return contains(p.displayName(), term) || contains(p.getSpecialty(), term)
                || contains(p.getDescription(), term) || contains(p.getCategory(), term)
                || contains(p.getJobTitle(), term);
    }

    private boolean contains(String value, String term) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(term);
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}
