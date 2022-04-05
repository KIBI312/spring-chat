package com.example.chat.repository;

import java.util.List;

import com.example.chat.entity.Message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findTop30ByChatIdOrderByTimestampDesc(Long id);
    List<Message> findAllByChatId(Long id, Pageable pageable);
}
