package com.example.chat.repository;

import java.util.List;

import com.example.chat.entity.Participant;

import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends CrudRepository<Participant, Long> {
    List<Participant> findByUnameId(Long id);
    List<Participant> findByChatId(Long id);
}
