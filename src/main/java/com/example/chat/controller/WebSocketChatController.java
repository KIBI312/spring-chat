package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.repository.MessageRepository;
import com.example.chat.util.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.slf4j.Logger;

@Controller
public class WebSocketChatController {
    
    Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;


    @GetMapping("/sockjs-message")
    public String webSocketWithSockJs() {
        return "sockjs-message";
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor sha, @Payload Message message) throws Exception{
        String sender = sha.getUser().getName();
        Message chatMessage = new Message(message.getChatId(), sender,
                            message.getToUname(), StringUtils.getCurrentTimestamp(), message.getContent());
        messageRepository.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(message.getToUname(), "/queue/messages", chatMessage);
    }
}
