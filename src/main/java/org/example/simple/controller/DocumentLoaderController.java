package org.example.simple.controller;

import lombok.extern.log4j.Log4j2;
import org.example.simple.service.DocumentLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@Log4j2
public class DocumentLoaderController {
    private final DocumentLoader documentLoader;

    public DocumentLoaderController(DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received file {} to upload.", file.getOriginalFilename());
            documentLoader.loadFromFile(file);
            log.info("File loaded.");
            return ResponseEntity.ok("File processed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/load-directory")
    public ResponseEntity<String> loadFromDirectory(@RequestParam("path") String directoryPath) {
        try {
            documentLoader.loadFromDirectory(directoryPath);
            return ResponseEntity.ok("Directory processed successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing directory: " + e.getMessage());
        }
    }
}
