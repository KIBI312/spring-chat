package com.example.chat.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupChatDto {
    private String chatName;
    private List<String> participants;

    public GroupChatDto(String chatName, List<String> participants) {
        this.chatName = chatName;
        this.participants = participants;
    }
}
