package com.example.chat.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.SafeHtml;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MessageDto {
    private String toUname;
    @NotBlank(message = "Mandatory")
    @SafeHtml
    private String content;
    private Boolean isGroup;

    public MessageDto(String toUname, String content, Boolean isGroup) {
        this.toUname = toUname;
        this.content = content;
        this.isGroup = isGroup;
    }
}
