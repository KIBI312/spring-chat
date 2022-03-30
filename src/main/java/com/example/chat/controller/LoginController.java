// package com.example.chat.controller;

// import com.example.chat.repository.UserRepository;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// @RequestMapping("/login")
// public class LoginController {

//     private UserRepository userRepository;
//     private PasswordEncoder passwordEncoder;

//     public LoginController() {
//         this.userRepository = userRepository;
//         this.passwordEncoder = passwordEncoder;
//     }
    
//     @GetMapping
//     public String loginForm(){
//         return "login";
//     }

//     @PostMapping
//     public String processLogin(){

//         return "success";
//     }
// }
