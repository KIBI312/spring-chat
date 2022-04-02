package com.example.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "message_id", nullable = false, columnDefinition = "int(11) NOT NULL DEFAULT '0'")
    private Long id;
    @Column(name = "from_user_id", nullable = false, columnDefinition = "int(11) NOT NULL")
    private String fromUname;
    @Column(name = "to_user_id", nullable = false, columnDefinition = "int(11) NOT NULL")
    private String toUname;

}
