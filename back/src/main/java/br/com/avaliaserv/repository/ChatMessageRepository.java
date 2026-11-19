package br.com.avaliaserv.repository;

import br.com.avaliaserv.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId);

    @Query("select m from ChatMessage m where m.senderEmail = :email or m.recipientEmail = :email order by m.createdAt desc")
    List<ChatMessage> findAllInvolving(@Param("email") String email);
}
