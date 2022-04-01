package com.example.chat.util;

import java.util.ArrayList;
import java.util.Map;

import com.example.chat.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class UserInterceptor implements ChannelInterceptor {
    
    Logger logger = LoggerFactory.getLogger(ChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if(raw instanceof Map) {
                Object name = ((Map) raw).get("username");
                if(name instanceof ArrayList) {
                    logger.debug("New user principal assigned");
                    stompHeaderAccessor.setUser(new User(((ArrayList) name).get(0).toString()));
                }
            }
        }
        return message;
    }
    
}
