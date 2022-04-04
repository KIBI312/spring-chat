package com.example.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "int(11) NOT NULL")
    private Long id;
    @Column(name = "message_id", nullable = false, columnDefinition = "int(11) NOT NULL DEFAULT '0'")
    private Long chatId;
    @Column(name = "user_id", nullable = false, columnDefinition = "int(11) NOT NULL")
    private String unameId;


}
