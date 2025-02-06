package org.example.simple.service;

import org.example.simple.model.Document;
import org.example.simple.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.simple.util.Utils.parseEmbedding;

@Service
public class ChatService {
    private final OllamaService ollamaService;
    private final DocumentRepository documentRepository;

    public ChatService(OllamaService ollamaService, DocumentRepository documentRepository) {
        this.ollamaService = ollamaService;
        this.documentRepository = documentRepository;
    }

    public String processMessage(String message) {
        // Generate embedding for the query
        double[] queryEmbedding = parseEmbedding(ollamaService.generateEmbedding(message));

        // Retrieve relevant documents
        List<Document> relevantDocs = documentRepository.findNearestNeighbors(queryEmbedding, 3);

        // Combine relevant documents into context
        String context = relevantDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // Generate response using the context
        return ollamaService.generateResponse(message, context);
    }
}
