package br.com.avaliaserv.controller;

import br.com.avaliaserv.dto.ChatDtos.ConversationResponse;
import br.com.avaliaserv.dto.ChatDtos.MessageResponse;
import br.com.avaliaserv.dto.ChatDtos.SendMessageRequest;
import br.com.avaliaserv.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/conversations")
    public List<ConversationResponse> conversations() {
        return chatService.conversations();
    }

    @GetMapping("/messages")
    public List<MessageResponse> history(@RequestParam String with) {
        return chatService.history(with);
    }

    @PostMapping("/messages")
    public MessageResponse send(@Valid @RequestBody SendMessageRequest req) {
        return chatService.send(req);
    }
}
