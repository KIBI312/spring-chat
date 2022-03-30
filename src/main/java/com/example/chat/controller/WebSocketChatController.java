package com.example.chat.controller;

import com.example.chat.entity.Message;
import com.example.chat.repository.MessageRepository;

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
    private MessageRepository messageRepository;


    @GetMapping("/sockjs-message")
    public String webSocketWithSockJs() {
        return "sockjs-message";
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor sha, @Payload Message message) throws Exception{
        String sender = sha.getUser().getName();
        Message chatMessage = new Message(message.getChatId(),message.getFromUname(),
                            message.getToUname(), message.getTimestamp(), message.getContent());
        logger.debug(chatMessage.getFromUname()+"~"+chatMessage.getToUname()+"~"+chatMessage.getTimestamp());
        messageRepository.save(chatMessage);
        if(!sender.equals(message.getToUname())){
            simpMessagingTemplate.convertAndSendToUser(sender, "/queue/messages", chatMessage);
        }
        simpMessagingTemplate.convertAndSendToUser(message.getToUname(), "/queue/messages", chatMessage);
    }
}
