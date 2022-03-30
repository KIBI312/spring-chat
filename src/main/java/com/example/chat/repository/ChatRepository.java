package com.example.chat.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.chat.entity.Chat;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface ChatRepository extends CassandraRepository<Chat, UUID> {
    
    Optional<Chat> findById(UUID id);
    Optional<Chat> findByFromUname(UUID id);
    Optional<Chat> findByToUname(UUID id);
    

}
