package com.example.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
public class Chat {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "message_id", nullable = false, columnDefinition = "int(11) NOT NULL")
    private Long id;
    @Column(name = "is_group", nullable = false, columnDefinition = "boolean")
    private final Boolean isGroup;

    public Chat(Boolean isGroup) {
        this.isGroup = isGroup;
    }

}
