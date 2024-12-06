package com.spring.backend.Service.Implemenatation;

import com.spring.backend.Exceptions.MessageResourceNotFoundException;
import com.spring.backend.Model.Chat;
import com.spring.backend.Model.Message;
import com.spring.backend.Model.User;
import com.spring.backend.Repository.MessageRepository;
import com.spring.backend.Repository.UserRepository;
import com.spring.backend.Service.Abstraction.MessageService;
import com.spring.backend.Service.Abstraction.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService {
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final MessageRepository messageRepository;

    @Override
    public Message sendMessage(Long senderId, Long projectId, String content) {
        Message message=new Message();

        User sender=userRepository.findById(senderId).orElseThrow(()->new UsernameNotFoundException("User not found"));
        Chat chat=projectService.getProjectById(projectId).getChat();

        message.setContent(content);
        message.setSender(sender);
        message.setCreatedAt(LocalDateTime.now());
        message.setChat(chat);

        Message savedMessages=messageRepository.save(message);
        chat.getMessages().add(savedMessages);
        return savedMessages;

    }

    @Override
    public List<Message> getMessageByProjectId(Long projectId) {
        Chat chat=projectService.getChatByProjectId(projectId);
        List<Message> messagesHistory=messageRepository.findByChatIdOrderByCreatedAtDesc(chat.getId()).orElseThrow(
                ()->new MessageResourceNotFoundException("Message resource not found")
        );
        return messagesHistory;
    }
}
