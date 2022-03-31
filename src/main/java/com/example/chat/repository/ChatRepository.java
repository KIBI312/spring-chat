package com.example.chat.repository;

import java.util.Optional;
import java.util.UUID;

import com.example.chat.entity.Chat;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface ChatRepository extends CassandraRepository<Chat, UUID> {
    @Query(allowFiltering=true)
    Chat findByFromUnameAndToUname(String fromUname, String toUname);
    Optional<Chat> findById(UUID id);

}
