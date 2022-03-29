package com.example.chat;

import com.example.chat.entity.User;
import com.example.chat.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}
 
	// public PasswordEncoder passwordEncoder;


	// @Bean
	// public CommandLineRunner dataLoader(UserRepository repo, PasswordEncoder passwordEncoder) {
	// 	return new CommandLineRunner() {
	// 		@Override
	// 		public  void run(String... args) throws Exception {
	// 			repo.save(new User("test@test.su", "test", passwordEncoder.encode("test")));
	// 			repo.save(new User("test@test.su", "test1", passwordEncoder.encode("test1")));
	// 		}
	// 	};
	// }

}
