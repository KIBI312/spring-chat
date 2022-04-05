package com.example.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, columnDefinition = "int(11) NOT NULL")
    private Long id;
    @Column(name = "message_id", nullable = false, columnDefinition = "int(11) NOT NULL DEFAULT '0'")
    private Long chatId;
    @Column(name = "from_user_id", nullable = false, columnDefinition = "int(11) NOT NULL")
    private String fromUname;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME")
    private String timestamp;
    @Column(name = "message", columnDefinition = "mediumtext COLLATE utf8_unicode_ci NOT NULL")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "enum('unread','read') COLLATE utf8_unicode_ci NOT NULL DEFAULT 'unread'")
    private Status status;

    public static enum Status {
        read, unread
    }

    public Message(Long id) {
        this.id = id;
    }
    
    public Message(Long chatId, String fromUname, String timestamp, String content, Status status) {
        this.chatId = chatId;
        this.fromUname = fromUname;
        this.timestamp = timestamp;
        this.content = content;
        this.status = status;
    }
}
