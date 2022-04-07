package com.example.chat;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;

import com.example.chat.controller.WebSocketChatController;

@SpringBootTest
class ChatApplicationTests {

	@Autowired
	private WebSocketChatController webSocketChatController;

	final HttpUriRequest request = new HttpGet("z");

	@Test
	void contextLoads() {

	}

	
}
