package br.com.avaliaserv.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfessionalProfile profile;

    private String authorName;
    private String authorEmail;

    @Column(length = 1000)
    private String text;

    private int rating;

    @Builder.Default
    private LocalDate date = LocalDate.now();
}
