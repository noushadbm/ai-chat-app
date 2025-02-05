package org.example.simple.controller;

import org.example.simple.model.ChatMessage;
import org.example.simple.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatMessage chat(@RequestBody ChatMessage message) {
        System.out.println("Request received");
        String response = chatService.processMessage(message.getMessage());
        System.out.println("Returning response.");

        ChatMessage responseMessage = new ChatMessage();
        responseMessage.setMessage(response);
        responseMessage.setRole("assistant");

        return responseMessage;
    }
}
