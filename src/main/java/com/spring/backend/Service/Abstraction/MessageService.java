package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId,Long projectId,String content);
    List<Message> getMessageByProjectId(Long projectId);
}
