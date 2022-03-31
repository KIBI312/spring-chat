package com.example.chat.repository;

import java.util.List;
import java.util.UUID;

import com.example.chat.entity.Message;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface MessageRepository extends CassandraRepository<Message, UUID> {
    @Query(allowFiltering=true)
    List<Message> findByChatIdOrderByTimestampDesc(UUID id);

}
