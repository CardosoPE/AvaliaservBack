package br.com.avaliaserv.controller;

import br.com.avaliaserv.dto.ProfessionalDtos.ProfileResponse;
import br.com.avaliaserv.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public List<ProfileResponse> list() {
        return favoriteService.list();
    }

    @GetMapping("/ids")
    public List<Long> ids() {
        return favoriteService.ids();
    }

    @PostMapping("/{profileId}/toggle")
    public Map<String, Object> toggle(@PathVariable Long profileId) {
        boolean favorited = favoriteService.toggle(profileId);
        return Map.of("profileId", profileId, "favorited", favorited);
    }
}
