package br.com.avaliaserv.dto;

import br.com.avaliaserv.model.ServiceRequest;
import jakarta.validation.constraints.NotBlank;

public class RequestDtos {

    public record CreateRequest(
            @NotBlank String title,
            @NotBlank String description,
            String category,
            Double budget) {}

    public record RequestResponse(
            Long id, String title, String description, String category, Double budget,
            String status, String createdAt,
            String clientName, String clientEmail, String clientCity, String clientPhoto) {

        public static RequestResponse from(ServiceRequest r) {
            var c = r.getClient();
            return new RequestResponse(
                    r.getId(), r.getTitle(), r.getDescription(), r.getCategory(), r.getBudget(),
                    r.getStatus().name().toLowerCase(), r.getCreatedAt().toString(),
                    c.getName(), c.getEmail(), c.getCity(), c.getPhoto());
        }
    }
}
