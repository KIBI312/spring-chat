// package com.example.chat.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
// import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
// import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

// @Configuration
// @EnableWebSocketMessageBroker
// public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    
//     @Override
//     protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//         messages.simpDestMatchers("/secured/**").authenticated()
//                 .anyMessage().authenticated();
//     }

    
    
// }
