package com.example.chat.repository;

import java.util.Optional;

import com.example.chat.entity.Chat;

import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findByChatName(String chatName);
    Optional<Chat> findById(Long id);
}
