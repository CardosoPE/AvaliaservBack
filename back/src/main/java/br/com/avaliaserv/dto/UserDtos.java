package br.com.avaliaserv.dto;

import br.com.avaliaserv.model.User;

public class UserDtos {

    public record UserResponse(
            Long id, String name, String email, String type, String photo,
            String phone, String city, String gender, String birth, String bio,
            Long profileId) {

        public static UserResponse from(User u) {
            return new UserResponse(
                    u.getId(), u.getName(), u.getEmail(), u.getType().name().toLowerCase(),
                    u.getPhoto(), u.getPhone(), u.getCity(), u.getGender(),
                    u.getBirth() != null ? u.getBirth().toString() : null,
                    u.getBio(),
                    u.getProfile() != null ? u.getProfile().getId() : null);
        }
    }

    public record UpdateUserRequest(
            String name, String phone, String city, String bio, String photo,
            ProfessionalDtos.ProfileRequest profile) {}
}
