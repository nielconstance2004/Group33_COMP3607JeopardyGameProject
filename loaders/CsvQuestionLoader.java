package com.jeopardy.loader;

import com.jeopardy.model.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
 

public class CsvQuestionLoader implements QuestionLoader {
    @Override
    public List<Question> load(File file) throws Exception {
        List<Question> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",(?=(?:[^]*[^]*)*[^]*$)");
                // naive: allow header detection
                if (!headerSkipped) {
                    boolean looksLikeHeader = line.toLowerCase().contains("category") || line.toLowerCase().contains("question");
                    if (looksLikeHeader) { headerSkipped = true; continue; }
                    headerSkipped = true;
                }
                // Expecting at least: category,value,question,answer
                if (parts.length < 4) continue;
                String category = parts[0].trim().replaceAll('"'+"", "");
                int value = 0;
                try { value = Integer.parseInt(parts[1].trim()); } catch (Exception ex) { value = 0; }
                String questionText = parts[2].trim().replaceAll('"'+"", "");
                String answer = parts[3].trim().replaceAll('"'+"", "");
        list.add(new com.jeopardy.model.QuestionBuilder()
            .category(category)
            .value(value)
            .text(questionText)
            .answer(answer)
            .build());
            }
        }
        return list;
    }
}