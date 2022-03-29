package com.example.chat.repository;

import java.util.UUID;

import com.example.chat.entity.User;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface UserRepository extends CassandraRepository<User, UUID> {
    @Query(allowFiltering=true)
    User findByUsername(String username);
}
