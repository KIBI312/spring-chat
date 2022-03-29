package com.example.chat.repository;

import java.util.List;
import java.util.UUID;

import com.example.chat.entity.Message;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface MessageRepository extends CassandraRepository<Message, UUID> {
    
    List<Message> findByChatId(UUID id);

}
