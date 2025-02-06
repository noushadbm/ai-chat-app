package org.example.simple.service;

import lombok.extern.log4j.Log4j2;
import org.example.simple.model.Document;
import org.example.simple.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.simple.util.Utils.parseEmbedding;
import static org.example.simple.util.Utils.splitIntoChunks;

@Service
@Log4j2
public class DocumentLoader {
    private final DocumentRepository documentRepository;
    private final OllamaService ollamaService;
    private final PdfDocumentProcessor pdfDocumentProcessor;

    public DocumentLoader(
            DocumentRepository documentRepository,
            OllamaService ollamaService,
            PdfDocumentProcessor pdfDocumentProcessor
    ) {
        this.documentRepository = documentRepository;
        this.ollamaService = ollamaService;
        this.pdfDocumentProcessor = pdfDocumentProcessor;;
    }

    public void loadFromFile(MultipartFile file) throws Exception {
        String content;
        String originalFilename = file.getOriginalFilename();

        // Determine file type and extract content
        if (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf")) {
            content = pdfDocumentProcessor.extractTextFromPdf(file);
            log.info("Extracted content: {}", content);
        } else {
            content = new BufferedReader(
                    new InputStreamReader(file.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
        }

        // Split content into chunks (e.g., paragraphs)
        List<String> chunks = splitIntoChunks(content, 500)
                .stream().filter(item -> StringUtils.hasText(item))
                .toList(); // 500 characters per chunk

        List<Document> documents = new ArrayList<>();
        for (String chunk : chunks) {
            Document doc = new Document();
            doc.setContent(chunk);

            try {
                // Generate embedding, handling potential errors
                String embeddingStr = ollamaService.generateEmbedding(chunk);
                doc.setEmbedding(parseEmbedding(embeddingStr));
                documents.add(doc);
            } catch (Exception ex) {
                log.error("Error while loading from file.", ex);
            }
        }

        documentRepository.saveAll(documents);
    }

    public void loadFromDirectory(String directoryPath) throws Exception {
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String filename = path.toString().toLowerCase();
                    return filename.endsWith(".txt") || filename.endsWith(".pdf");
                })
                .forEach(path -> {
                    try {
                        String content;
                        if (path.toString().toLowerCase().endsWith(".pdf")) {
                            content = Files.readAllLines(path).stream()
                                    .collect(Collectors.joining("\n"));
                        } else {
                            content = Files.readString(path);
                        }

                        List<String> chunks = splitIntoChunks(content, 500);

                        List<Document> documents = new ArrayList<>();
                        for (String chunk : chunks) {
                            Document doc = new Document();
                            doc.setContent(chunk);

                            try {
                                String embeddingStr = ollamaService.generateEmbedding(chunk);
                                doc.setEmbedding(parseEmbedding(embeddingStr));
                                documents.add(doc);
                            } catch (Exception e) {
                                log.error("Failed to process chunk: " + e.getMessage());
                            }
                        }

                        documentRepository.saveAll(documents);
                    } catch (Exception e) {
                        log.error("Failed to process file: " + path, e);
                        throw new RuntimeException("Failed to process file: " + path, e);
                    }
                });
    }
}
