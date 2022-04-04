package com.example.chat.controller;

import java.util.List;
import java.util.Optional;

import com.example.chat.dto.GroupChat;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.entity.Message.Status;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.service.ChatService;
import com.example.chat.service.MessageService;
import com.example.chat.util.DateTimeUtil;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

@RestController
public class WebSocketChatController {
    
    Logger logger = LoggerFactory.getLogger(WebSocketChatController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired 
    private ChatRepository chatRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;


    @GetMapping("/sockjs-message")
    public String webSocketWithSockJs() {
        return "sockjs-message";
    }

//     @GetMapping("/messages/{fromUname}/{toUname}")
//     public ResponseEntity<?> findChatMessages(@PathVariable String fromUname, @PathVariable String toUname) {
//         if(chatRepository.findByFromUnameAndToUname(fromUname, toUname) == null) {
//             Chat chat = new Chat(fromUname, toUname);
//             Chat reverseChat = new Chat(toUname, fromUname);
//             chatRepository.save(chat);
//             chatRepository.save(reverseChat);
//         }
//         Chat currentChat = chatRepository.findByFromUnameAndToUname(fromUname, toUname);
//         Long chatId = currentChat.getId();
//         List<Message> oldMessages = messageRepository.findByChatIdOrderByTimestampDesc(chatId);
//         return ResponseEntity.ok(oldMessages);
//     }

    @PostMapping("/newchat")
    public void createNewChat(@RequestBody GroupChat chat) {
        logger.error(chat.getParticipants().toString());
        chatService.createGroupChat(chat.getParticipants(), chat.getChatName());
    }

    @PostMapping("/chatid")
    public String getChatId(@RequestBody String chatName) {
        logger.error(chatName);
        Long chatId = chatRepository.findByChatName(chatName).getId();
        return chatId.toString();
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor headerAccessor, @Payload Message message) throws Exception{
        String fromUname = headerAccessor.getUser().getName();
        Chat currentChat = chatRepository.findById(message.getChatId()).get();
        Message chatMessage = new Message(currentChat.getId(), fromUname,
                            DateTimeUtil.getCurrentTimestamp(), message.getContent(), Status.unread);
        messageRepository.save(chatMessage);
        chatService.getChatParticipantIds(currentChat.getId()).stream().filter(p -> p != fromUname).forEach((p) -> {
            simpMessagingTemplate.convertAndSendToUser(p, "/queue/messages", chatMessage);
        });
    }
}