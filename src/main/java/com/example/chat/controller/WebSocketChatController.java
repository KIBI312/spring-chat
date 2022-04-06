package com.example.chat.controller;

import java.util.List;
import java.util.Optional;

import com.example.chat.dto.GroupChatDto;
import com.example.chat.dto.MessageDto;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.entity.Message.Status;
import com.example.chat.exception.WrongValueException;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.service.ChatService;
import com.example.chat.service.MessageService;
import com.example.chat.util.DateTimeUtil;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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
import org.apache.catalina.connector.Response;
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


    // @GetMapping("/messages/{fromUname}/{chatId}")
    // public ResponseEntity<?> findChatMessages(@PathVariable String fromUname,@PathVariable Long chatId) {
    //     messageService.updateStatuses(chatId, fromUname);
    //     List<Message> oldMessages = messageRepository.findTop30ByChatIdOrderByTimestampDesc(chatId);
    //     return ResponseEntity.ok(oldMessages);
    // }


    @GetMapping("/previous/{chatId}/{fromUname}/{pageId}")
    public ResponseEntity<?> findPaginatedChatMessages(@PathVariable Long chatId, @PathVariable String fromUname,@PathVariable int pageId) {
        try {
            messageService.updateStatuses(chatId, fromUname);
            List<Message> oldMessages = messageService.getPaginatedMessages(chatId, pageId);
            return ResponseEntity.ok(oldMessages);
            
        } catch (WrongValueException exc) {
            return ResponseEntity.ok(exc.getMessage());
        }
    }

    @PostMapping("/newchat")
    public ResponseEntity<?> createNewChat(@RequestBody GroupChatDto chat) {
        try {
            chatService.createGroupChat(chat.getParticipants(), chat.getChatName());
            return ResponseEntity.ok(chat);
        }
        catch(WrongValueException exc) {
            return ResponseEntity.ok(exc.getMessage());
        }
        catch(DataIntegrityViolationException exc) {
            return ResponseEntity.ok("Chat with this name already exists");
        }
    }

    @PostMapping("/chatid/{chatName}")
    public ResponseEntity<?> getChatId(@PathVariable String chatName) {
        try {
            if(chatRepository.findByChatName(chatName).isEmpty()) throw new WrongValueException("Chat with this name doesnt exist");
            Chat chat = chatRepository.findByChatName(chatName).get();
            return ResponseEntity.ok(chat);
        } catch (WrongValueException exc) {
            return ResponseEntity.ok(exc.getMessage());
        }
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor headerAccessor, @Payload MessageDto message) throws Exception{
        try {
            String fromUname = headerAccessor.getUser().getName();
            Long chatId;
            if(message.getIsGroup()){
                if(chatRepository.findByChatName(message.getToUname()).isEmpty()) throw new WrongValueException("Chat with this name doesnt exist");
                chatId = chatRepository.findByChatName(message.getToUname()).get().getId();
            } else {
                chatId = chatService.createOnetoOneIfNotExist(fromUname, message.getToUname());
            }
            Message chatMessage = new Message(chatId, fromUname,
                                DateTimeUtil.getCurrentTimestamp(), message.getContent(), Status.unread);
            messageRepository.save(chatMessage);
            chatService.getChatParticipantIds(chatId).stream().filter(p -> p != fromUname).forEach((p) -> {
                simpMessagingTemplate.convertAndSendToUser(p, "/queue/messages", chatMessage);
            });
        } catch (WrongValueException exc) {
            simpMessagingTemplate.convertAndSendToUser(headerAccessor.getUser().getName(), "/queue/messages", exc.getMessage());
        }
    }
}