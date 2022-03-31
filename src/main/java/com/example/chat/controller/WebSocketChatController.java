package com.example.chat.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.LogRecord;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.util.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;

@Controller
public class WebSocketChatController {
    
    Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired 
    private ChatRepository chatRepository;


    @GetMapping("/sockjs-message")
    public String webSocketWithSockJs() {
        return "sockjs-message";
    }

    @GetMapping("/messages/{fromUname}/{toUname}")
    public ResponseEntity<?> findChatMessages(@PathVariable String fromUname, @PathVariable String toUname) {
        if(chatRepository.findByFromUnameAndToUname(fromUname, toUname) == null) {
            Chat chat = new Chat(fromUname, toUname);
            Chat reverseChat = new Chat(toUname, fromUname);
            chatRepository.save(chat);
            chatRepository.save(reverseChat);
        }
        Chat currentChat = chatRepository.findByFromUnameAndToUname(fromUname, toUname);
        UUID chatId = currentChat.getId();
        List<Message> oldMessages = messageRepository.findByChatIdOrderByTimestampDesc(chatId);
        return ResponseEntity.ok(oldMessages);
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor sha, @Payload Message message) throws Exception{
        String fromUname = sha.getUser().getName();
        if(chatRepository.findByFromUnameAndToUname(fromUname, message.getToUname()) == null) {
            if(fromUname.equals(message.getToUname())){
                Chat chat = new Chat(fromUname, message.getToUname());
                chatRepository.save(chat);
            } else {
                logger.error(fromUname+"~"+message.getToUname());
                Chat chat = new Chat(fromUname, message.getToUname());
                Chat reverseChat = new Chat(message.getToUname(), fromUname);
                chatRepository.save(chat);
                chatRepository.save(reverseChat);
            }
        }
        Chat currentChat = chatRepository.findByFromUnameAndToUname(fromUname, message.getToUname());
        Message chatMessage = new Message(currentChat.getId(), fromUname,
                            message.getToUname(), StringUtils.getCurrentTimestamp(), message.getContent());
        messageRepository.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(message.getToUname(), "/queue/messages", chatMessage);
    }
}
