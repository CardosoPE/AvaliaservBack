package br.com.avaliaserv.service;

import br.com.avaliaserv.dto.ChatDtos.ConversationResponse;
import br.com.avaliaserv.dto.ChatDtos.MessageResponse;
import br.com.avaliaserv.dto.ChatDtos.SendMessageRequest;
import br.com.avaliaserv.exception.ApiException;
import br.com.avaliaserv.model.ChatMessage;
import br.com.avaliaserv.model.User;
import br.com.avaliaserv.repository.ChatMessageRepository;
import br.com.avaliaserv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    @Transactional
    public MessageResponse send(SendMessageRequest req) {
        User sender = currentUser.get();
        User recipient = userRepository.findByEmailIgnoreCase(req.recipientEmail().trim())
                .orElseThrow(() -> ApiException.notFound("Destinatario nao encontrado."));

        if (sender.getEmail().equalsIgnoreCase(recipient.getEmail())) {
            throw ApiException.badRequest("Nao e possivel conversar consigo mesmo.");
        }

        ChatMessage message = ChatMessage.builder()
                .roomId(ChatMessage.roomOf(sender.getEmail(), recipient.getEmail()))
                .senderEmail(sender.getEmail())
                .senderName(sender.getName())
                .recipientEmail(recipient.getEmail())
                .requestId(req.requestId())
                .text(req.text().trim())
                .build();

        return MessageResponse.from(messageRepository.save(message));
    }

    /** Historico da conversa com outro usuario; marca as mensagens recebidas como lidas. */
    @Transactional
    public List<MessageResponse> history(String otherEmail) {
        String me = currentUser.email();
        String roomId = ChatMessage.roomOf(me, otherEmail.trim().toLowerCase());

        List<ChatMessage> messages = messageRepository.findByRoomIdOrderByCreatedAtAsc(roomId);
        messages.stream()
                .filter(m -> !m.getSenderEmail().equalsIgnoreCase(me) && !m.isReadFlag())
                .forEach(m -> m.setReadFlag(true));

        return messages.stream().map(MessageResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> conversations() {
        String me = currentUser.email();
        Map<String, List<ChatMessage>> byRoom = new LinkedHashMap<>();

        for (ChatMessage m : messageRepository.findAllInvolving(me)) {
            byRoom.computeIfAbsent(m.getRoomId(), k -> new ArrayList<>()).add(m);
        }

        List<ConversationResponse> result = new ArrayList<>();
        byRoom.forEach((roomId, messages) -> {
            messages.sort(Comparator.comparing(ChatMessage::getCreatedAt));
            ChatMessage last = messages.get(messages.size() - 1);

            String otherEmail = last.getSenderEmail().equalsIgnoreCase(me)
                    ? last.getRecipientEmail() : last.getSenderEmail();

            User other = userRepository.findByEmailIgnoreCase(otherEmail).orElse(null);
            long unread = messages.stream()
                    .filter(m -> !m.getSenderEmail().equalsIgnoreCase(me) && !m.isReadFlag())
                    .count();

            result.add(new ConversationResponse(
                    roomId, otherEmail,
                    other != null ? other.getName() : otherEmail,
                    other != null ? other.getPhoto() : null,
                    last.getText(), last.getCreatedAt().toString(), unread));
        });

        result.sort(Comparator.comparing(ConversationResponse::lastMessageAt).reversed());
        return result;
    }
}
