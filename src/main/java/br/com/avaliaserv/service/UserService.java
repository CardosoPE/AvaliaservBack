package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.UserDtos.UpdateUserRequest;
import br.com.avaliaserv.dto.UserDtos.UserResponse;
import br.com.avaliaserv.model.ProfessionalProfile;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.model.UserType;
import br.com.avaliaserv.repository.FavoriteRepository;
import br.com.avaliaserv.repository.ProfessionalProfileRepository;
import br.com.avaliaserv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfessionalProfileRepository profileRepository;
    private final FavoriteRepository favoriteRepository;
    private final CurrentUser currentUser;
    private final ProfileMapper profileMapper;

    @Transactional(readOnly = true)
    public UserResponse me() {
        return UserResponse.from(currentUser.get());
    }

    @Transactional
    public UserResponse update(UpdateUserRequest req) {
        User user = currentUser.get();

        if (req.name() != null && !req.name().isBlank()) user.setName(req.name().trim());
        if (req.phone() != null) user.setPhone(req.phone());
        if (req.city() != null)  user.setCity(req.city());
        if (req.bio() != null)   user.setBio(req.bio());
        if (req.photo() != null) user.setPhoto(req.photo().isBlank() ? null : req.photo());

        if (user.getType() == UserType.PROFESSIONAL && req.profile() != null) {
            ProfessionalProfile profile = profileRepository.findByUserId(user.getId())
                    .orElseGet(() -> ProfessionalProfile.builder().user(user).build());
            profileMapper.apply(profile, req.profile());
            profileRepository.save(profile);
            user.setProfile(profile);
        }

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteAccount() {
        User user = currentUser.get();
        favoriteRepository.deleteByUserId(user.getId());
        profileRepository.findByUserId(user.getId()).ifPresent(profileRepository::delete);
        userRepository.delete(user);
    }
}
