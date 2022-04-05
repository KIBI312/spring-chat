package com.example.chat.service;

import java.util.List;

import com.example.chat.entity.Message;
import com.example.chat.entity.Message.Status;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.ParticipantRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChatRepository chatRepository;

    public List<Message> getMessages(Long chatId) {
        return messageRepository.findTop30ByChatIdOrderByTimestampDesc(chatId);
    }

    public void updateStatuses(Long chatId, String fromUname) {
        getMessages(chatId).stream().filter(m -> !m.getFromUname().equals(fromUname)).forEach((msg) -> {
            msg.setStatus(Status.read);
            messageRepository.save(msg);
        });
    }


}
