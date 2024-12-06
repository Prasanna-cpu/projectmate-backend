package com.spring.backend.Repository;

import com.spring.backend.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select m from Message m where m.chat.id = ?1 order by m.createdAt DESC")
    Optional<List<Message>> findByChatIdOrderByCreatedAtDesc(Long chatId);
}
