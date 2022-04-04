package com.example.chat.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.chat.entity.Chat;
import com.example.chat.entity.Participant;
import com.example.chat.repository.ChatRepository;
import com.example.chat.repository.ParticipantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    
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
        Chat chat = new Chat(chatName, true);
        chatRepository.save(chat);
        participants.forEach((p) -> {
            Participant participant = new Participant(chat, p);
            participantRepository.save(participant);
        });
        return chat;
    }

    public Chat createOnetoOneChat(String fromUname, String toUname) {
        Chat chat = new Chat(String.format("%s_%s", fromUname, toUname), false);
        Participant participantFrom = new Participant(chat, fromUname);
        Participant participantTo = new Participant(chat, toUname);
        participantRepository.save(participantFrom);
        participantRepository.save(participantTo);
        return chat;
    }

}
