package com.example.chat.repository;

import java.util.UUID;

import com.example.chat.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);
}
