package br.com.avaliaserv.dto;

import br.com.avaliaserv.model.ChatMessage;
import jakarta.validation.constraints.NotBlank;

public class ChatDtos {

    public record SendMessageRequest(
            @NotBlank String recipientEmail,
            @NotBlank String text,
            Long requestId) {}

    public record MessageResponse(
            Long id, String roomId, String senderEmail, String senderName,
            String text, String createdAt, boolean read) {

        public static MessageResponse from(ChatMessage m) {
            return new MessageResponse(m.getId(), m.getRoomId(), m.getSenderEmail(),
                    m.getSenderName(), m.getText(), m.getCreatedAt().toString(), m.isReadFlag());
        }
    }

    public record ConversationResponse(
            String roomId, String otherEmail, String otherName, String otherPhoto,
            String lastMessage, String lastMessageAt, long unread) {}
}
