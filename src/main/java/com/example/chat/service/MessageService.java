package com.example.chat.service;

import java.util.List;

import com.example.chat.entity.Message;
import com.example.chat.entity.Message.Status;
import com.example.chat.exception.WrongValueException;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.repository.ParticipantRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
        if (chatRepository.findById(chatId).isEmpty()) throw new WrongValueException("Chat doesnt exist");
        Integer intval = Integer.parseInt(fromUname);
        if (participantRepository.findByUnameIdAndChatId(fromUname, chatId).isEmpty()) throw new WrongValueException("User doesnt exist in provided chat");
        getMessages(chatId).stream().filter(m -> !m.getFromUname().equals(fromUname)).forEach((msg) -> {
            msg.setStatus(Status.read);
            messageRepository.save(msg);
        }); 
    }

    public List<Message> getPaginatedMessages(Long chatId, int pageId) {
        Pageable sortedByTimestamp = PageRequest.of(pageId, 30, Sort.by("timestamp").descending());
        return messageRepository.findAllByChatId(chatId, sortedByTimestamp);
    }



}
