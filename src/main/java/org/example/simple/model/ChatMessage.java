package org.example.simple.model;

import lombok.Data;

@Data
public class ChatMessage {
    private String message;
    private String role; // user or assistant
}
