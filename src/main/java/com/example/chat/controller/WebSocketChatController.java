package com.example.chat.controller;

import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import com.example.chat.dto.GroupChatDto;
import com.example.chat.dto.MessageDto;
import com.example.chat.entity.Chat;
import com.example.chat.entity.Message;
import com.example.chat.entity.Message.Status;
import com.example.chat.exception.ErrorMessage;
import com.example.chat.exception.WrongValueException;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.MessageRepository;
import com.example.chat.service.ChatService;
import com.example.chat.service.MessageService;
import com.example.chat.util.DateTimeUtil;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "load paginated chat history by chatId, userId, pageId")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Messages found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong Values", content = {@Content(mediaType = "application/json", schema = @Schema(example = "{\n\"Error\": \"ErrorMessage\"\n}" ))}),
        @ApiResponse(responseCode = "500", description = "Broken request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/previous/{chatId}/{fromUname}/{pageId}")
    public ResponseEntity<?> findPaginatedChatMessages(@PathVariable Long chatId, @PathVariable String fromUname,@PathVariable int pageId) {
        try {
            messageService.updateStatuses(chatId, fromUname);
            List<Message> oldMessages = messageService.getPaginatedMessages(chatId, pageId);
            return ResponseEntity.ok(oldMessages);
        } catch(NumberFormatException exc) {
            return ResponseEntity.status(400).body(Collections.singletonMap("Error", "Unacceptable users id values"));
        } catch (WrongValueException exc) {
            return ResponseEntity.status(400).body(Collections.singletonMap("Error", exc.getMessage()));
        }
    }

    @Operation(summary = "create new group chat by chatName and list of participants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GroupChatDto.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong Values", content = {@Content(mediaType = "application/json", schema = @Schema(example = "{\n\"Error\": \"ErrorMessage\"\n}" ))}),
        @ApiResponse(responseCode = "500", description = "Broken request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    })
    @PostMapping("/newchat")
    public ResponseEntity<?> createNewChat(@RequestBody GroupChatDto chat) {
        try {
            chatService.createGroupChat(chat.getParticipants(), chat.getChatName());
            return ResponseEntity.ok(chat);
        }
        catch(WrongValueException exc) {
            return ResponseEntity.status(400).body(exc.getMessage());
        }
        catch(NumberFormatException exc) {
            return ResponseEntity.status(400).body(Collections.singletonMap("Error", "Unacceptable users id values"));
        }
        catch(DataIntegrityViolationException exc) {
            return ResponseEntity.status(400).body(Collections.singletonMap("Error", "Chat with this name already exists"));
        }
    }

    @Operation(summary = "get Chat Entity by chatName")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Chat.class))}),
        @ApiResponse(responseCode = "400", description = "Wrong Values", content = {@Content(mediaType = "application/json", schema = @Schema(example = "{\n\"Error\": \"ErrorMessage\"\n}" ))}),
        @ApiResponse(responseCode = "500", description = "Broken request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
    })
    @GetMapping("/chatid/{chatName}")
    public ResponseEntity<?> getChatId(@PathVariable String chatName) {
        try {
            if(chatRepository.findByChatName(chatName).isEmpty()) throw new WrongValueException("Chat with this name doesnt exist");
            Chat chat = chatRepository.findByChatName(chatName).get();
            return ResponseEntity.ok(chat);
        } catch (WrongValueException exc) {
            return ResponseEntity.status(400).body(exc.getMessage());
        }
    }

    @MessageMapping("/ws")
    public void send(SimpMessageHeaderAccessor headerAccessor, @Payload @Valid MessageDto message) throws Exception{
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