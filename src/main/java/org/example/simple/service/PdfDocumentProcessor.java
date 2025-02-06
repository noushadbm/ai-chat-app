package org.example.simple.service;

import lombok.extern.log4j.Log4j2;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@Log4j2
public class PdfDocumentProcessor {
    /**
     * Extract text content from a PDF file
     *
     * @param file MultipartFile containing PDF
     * @return Extracted text content as a string
     * @throws IOException If there's an error reading the PDF
     */
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            log.info("Total number of page in pdf file: {}", document.getNumberOfPages());
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // Optional: Configure text extraction
            pdfStripper.setSortByPosition(true);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(document.getNumberOfPages());

            return pdfStripper.getText(document);
        }
    }
}
