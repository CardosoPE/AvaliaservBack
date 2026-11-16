package br.com.avaliaserv.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professional_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProfessionalProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /** Perfis de demonstracao nao possuem usuario vinculado. */
    @Builder.Default
    private boolean demo = false;

    private String demoName;

    @Column(columnDefinition = "TEXT")
    private String demoPhoto;

    private String category;
    private String jobTitle;
    private String specialty;
    private String experience;

    @Column(length = 1000)
    private String description;

    private String whatsapp;
    private String whatsappRaw;
    private String whatsappCountry;

    private String location;
    private String serviceRadius;
    private String availability;
    private String serviceType;
    private String transport;

    private Double hourlyRate;
    private Double minValue;

    private String register;
    private String insurance;
    private String instagram;
    private String website;

    @Builder.Default
    private String status = "online";

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_payment_methods", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "method")
    @Builder.Default
    private List<String> paymentMethods = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_languages", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "language")
    @Builder.Default
    private List<String> languages = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profile_formations", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "formation")
    @Builder.Default
    private List<String> formations = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    public String displayName() {
        return user != null ? user.getName() : demoName;
    }

    public String displayPhoto() {
        return user != null ? user.getPhoto() : demoPhoto;
    }

    public String ownerEmail() {
        return user != null ? user.getEmail() : null;
    }
}
