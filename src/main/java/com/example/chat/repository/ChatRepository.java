package com.example.chat.repository;

import java.util.Optional;

import com.example.chat.entity.Chat;

import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, Long> {
    Chat findByFromUnameAndToUname(String fromUname, String toUname);
    Optional<Chat> findById(Long id);

}
