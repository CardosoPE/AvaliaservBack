package br.com.avaliaserv.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "chat_messages", indexes = @Index(name = "idx_room", columnList = "roomId"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String senderEmail;

    private String senderName;
    private String recipientEmail;
    private Long requestId;

    @Column(nullable = false, length = 2000)
    private String text;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private boolean readFlag = false;

    /** Sala canonica: os dois e-mails ordenados e unidos por "_". */
    public static String roomOf(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "_" + b : b + "_" + a;
    }
}
