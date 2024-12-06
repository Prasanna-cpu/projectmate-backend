package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Model.Chat;
import com.spring.backend.Repository.ChatRepository;
import com.spring.backend.Service.Abstraction.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class ChatServiceImplementation implements ChatService {

    private final ChatRepository chatRepository;
    @Override
    public Chat createChat(Chat chat) {
        Chat savedChat = chatRepository.save(chat);
        return savedChat;
    }
}
