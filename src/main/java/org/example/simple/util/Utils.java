package org.example.simple.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Utils {
    public static double[] parseEmbedding(String embeddingStr) {
        String[] values = embeddingStr.replaceAll("[\\[\\]]", "").split(",");
        //embeddingStr.replaceAll("[\\[\\]]", "").split(",");
        List<String> filteredValues = new ArrayList<>();
        for(String str: values) {
            //log.info("===> {}", str);
            if(StringUtils.hasText(str)) {
                filteredValues.add(str);
            }
        }
//        double[] embedding = new double[values.length];
//        for (int i = 0; i < values.length; i++) {
//            embedding[i] = Double.parseDouble(values[i].trim());
//        }
        double[] embedding = new double[filteredValues.size()];
        for (int i = 0; i < filteredValues.size(); i++) {
            embedding[i] = Double.parseDouble(filteredValues.get(i).trim());
        }
        return embedding;
    }

    public static List<String> splitIntoChunks(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        String[] paragraphs = text.split("\n\n");

        StringBuilder currentChunk = new StringBuilder();
        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() > chunkSize) {
                chunks.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder();
            }
            currentChunk.append(paragraph).append("\n\n");
        }

        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }

        return chunks;
    }
}
