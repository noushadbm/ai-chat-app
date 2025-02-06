package org.example.simple.controller;

import lombok.extern.log4j.Log4j2;
import org.example.simple.model.ChatMessage;
import org.example.simple.service.ChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatMessage chat(@RequestBody ChatMessage message) {
        log.info("Request received");
        String response = chatService.processMessage(message.getMessage());
        log.info("Returning response.");

        ChatMessage responseMessage = new ChatMessage();
        responseMessage.setMessage(response);
        responseMessage.setRole("assistant");

        return responseMessage;
    }
}
