package br.com.avaliaserv.dto;

import br.com.avaliaserv.model.ProfessionalProfile;
import br.com.avaliaserv.model.Review;

import java.util.List;

public class ProfessionalDtos {

    public record ProfileRequest(
            String category, String jobTitle, String specialty, String experience,
            String description, String whatsapp, String whatsappRaw, String whatsappCountry,
            String location, String serviceRadius, String availability, String serviceType,
            String transport, Double hourlyRate, Double minValue, String register,
            String insurance, String instagram, String website, String status,
            List<String> paymentMethods, List<String> languages, List<String> formations) {}

    public record ReviewResponse(Long id, String author, String text, int rating, String date) {
        public static ReviewResponse from(Review r) {
            return new ReviewResponse(r.getId(), r.getAuthorName(), r.getText(), r.getRating(),
                    r.getDate() != null ? r.getDate().toString() : null);
        }
    }

    public record ReviewRequest(String text, int rating) {}

    public record ProfileResponse(
            Long id, String userEmail, String name, String photo, String category,
            String jobTitle, String specialty, String experience, String description,
            String whatsapp, String location, String serviceRadius, String availability,
            String serviceType, String transport, Double hourlyRate, Double minValue,
            String register, String insurance, String instagram, String website, String status,
            List<String> paymentMethods, List<String> languages, List<String> formations,
            List<ReviewResponse> reviews, double averageRating) {

        public static ProfileResponse from(ProfessionalProfile p) {
            double avg = p.getReviews().isEmpty() ? 0
                    : p.getReviews().stream().mapToInt(Review::getRating).average().orElse(0);
            return new ProfileResponse(
                    p.getId(), p.ownerEmail(), p.displayName(), p.displayPhoto(), p.getCategory(),
                    p.getJobTitle(), p.getSpecialty(), p.getExperience(), p.getDescription(),
                    p.getWhatsapp(), p.getLocation(), p.getServiceRadius(), p.getAvailability(),
                    p.getServiceType(), p.getTransport(), p.getHourlyRate(), p.getMinValue(),
                    p.getRegister(), p.getInsurance(), p.getInstagram(), p.getWebsite(), p.getStatus(),
                    p.getPaymentMethods(), p.getLanguages(), p.getFormations(),
                    p.getReviews().stream().map(ReviewResponse::from).toList(),
                    Math.round(avg * 10) / 10.0);
        }
    }
}
