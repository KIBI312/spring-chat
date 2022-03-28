package com.example.chat.form;

import com.example.chat.entity.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class RegistrationForm {
    private String username;
    private String email;
    private String password;

    public User toUser(PasswordEncoder passwordEncoder){
        return new User(email,username,passwordEncoder.encode(password));
    }
}
