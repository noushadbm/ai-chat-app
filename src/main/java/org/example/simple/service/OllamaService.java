package org.example.simple.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {
    private final RestTemplate restTemplate;
    private final String ollamaApiUrl;
    private final ObjectMapper objectMapper;

    public OllamaService(@Value("${ollama.api.url}") String ollamaApiUrl) {
        this.ollamaApiUrl = ollamaApiUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String generateEmbedding(String text) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", "llama2");
        request.put("prompt", text);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        String response = restTemplate.postForObject(
                ollamaApiUrl + "/embeddings",
                entity,
                String.class
        );

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            return responseMap.get("embedding").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate embedding", e);
        }
    }

    public String generateResponse(String prompt, String context) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", "llama2");
        request.put("prompt", "Context: " + context + "\n\nQuestion: " + prompt + "\n\nAnswer:");
        request.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        String response = restTemplate.postForObject(
                ollamaApiUrl + "/generate",
                entity,
                String.class
        );

        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            return responseMap.get("response").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate response", e);
        }
    }
}
