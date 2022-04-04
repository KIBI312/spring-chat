package com.example.chat.service;

import java.util.List;

import com.example.chat.entity.Message;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.ParticipantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChatRepository chatRepository;

    public List<Message> getMessages(Long chatId) {
        return messageRepository.findByChatIdOrderByTimestampDesc(chatId);
    }

}
