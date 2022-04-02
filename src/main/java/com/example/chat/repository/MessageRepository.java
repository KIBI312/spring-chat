package com.example.chat.repository;

import java.util.List;

import com.example.chat.entity.Message;

import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findByChatIdOrderByTimestampDesc(Long id);

}
