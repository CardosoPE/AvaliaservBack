package br.com.avaliaserv.controller;

import br.com.avaliaserv.dto.ProfessionalDtos.ProfileResponse;
import br.com.avaliaserv.dto.ProfessionalDtos.ReviewRequest;
import br.com.avaliaserv.service.ProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionals")
@RequiredArgsConstructor
public class ProfessionalController {

    private final ProfessionalService professionalService;

    @GetMapping
    public List<ProfileResponse> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String availability) {
        return professionalService.search(q, category, city, availability);
    }

    @GetMapping("/{id}")
    public ProfileResponse byId(@PathVariable Long id) {
        return professionalService.byId(id);
    }

    @PostMapping("/{id}/reviews")
    public ProfileResponse review(@PathVariable Long id, @RequestBody ReviewRequest req) {
        return professionalService.addReview(id, req);
    }
}
