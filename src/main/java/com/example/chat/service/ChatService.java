package com.example.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Participant;
import com.example.chat.exception.WrongValueException;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.ParticipantRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    
 
    Logger logger = LoggerFactory.getLogger(ChatService.class);


    @Autowired
    ChatRepository chatRepository;
    @Autowired 
    ParticipantRepository participantRepository;

    public List<String> getChatParticipantIds(Long chatId){
        List<String> participantIds = participantRepository.findByChatId(chatId).stream()
                                      .map(Participant::getUnameId).collect(Collectors.toList());
        return participantIds;
    }

    public Chat createGroupChat(List<String> participants, String chatName) {
        if(chatName.length()<4) throw new WrongValueException("Min length of chat name 4 chars"); 
        Chat chat = new Chat(chatName, true);
        chatRepository.save(chat);
        participants.forEach((p) -> {
            Participant participant = new Participant(chat, p);
            participantRepository.save(participant);
        });
        return chat;
    }

    public Long createOnetoOneIfNotExist(String fromUname, String toUname) {
        String var1 = String.format("%s_%s", fromUname, toUname);
        String var2 = String.format("%s_%s", toUname, fromUname);
        logger.error(var1+"~"+var2);
        if(chatRepository.findByChatName(var1)==null & chatRepository.findByChatName(var2)==null) {
            Chat chat = new Chat(String.format("%s_%s", fromUname, toUname), false);
            chatRepository.save(chat);
            Participant participantFrom = new Participant(chat, fromUname);
            Participant participantTo = new Participant(chat, toUname);
            participantRepository.save(participantFrom);
            participantRepository.save(participantTo);
            return chat.getId();
        } else if(chatRepository.findByChatName(var1)!=null) return chatRepository.findByChatName(var1).getId();
        else return chatRepository.findByChatName(var2).getId();
        
    }

}
