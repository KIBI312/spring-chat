package com.example.chat.repository;

import java.util.List;
import java.util.Optional;

import com.example.chat.entity.Participant;

import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    List<Participant> findByUnameId(String id);
    List<Participant> findByChatId(Long id);
    Optional<Participant> findByUnameIdAndChatId(String unameId, Long chatId);
}
