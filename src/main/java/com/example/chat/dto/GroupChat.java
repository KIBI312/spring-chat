package com.example.chat.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupChat {
    private String chatName;
    private List<String> participants;

    public GroupChat(String chatName, List<String> participants) {
        this.chatName = chatName;
        this.participants = participants;
    }
}
