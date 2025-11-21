package com.jeopardy.loader;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jeopardy.model.Question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 

public class XmlQuestionLoader implements QuestionLoader {
    private final XmlMapper xml = new XmlMapper();

    @Override
    public List<Question> load(File file) throws Exception {
        // Try to parse as a root list or wrapper
        List<Question> out = new ArrayList<>();
        try {
            List<Map<String, Object>> arr = xml.readValue(file, List.class);
            for (Map<String, Object> m : arr) {
                String category = String.valueOf(m.getOrDefault("category", m.getOrDefault("Category", "")));
                int value = 0; try { value = Integer.parseInt(String.valueOf(m.getOrDefault("value", m.getOrDefault("Value", "0")))); } catch (Exception ignored) {}
                String text = String.valueOf(m.getOrDefault("question", m.getOrDefault("Question", "")));
                String answer = String.valueOf(m.getOrDefault("answer", m.getOrDefault("Answer", "")));
        out.add(new com.jeopardy.model.QuestionBuilder()
            .category(category)
            .value(value)
            .text(text)
            .answer(answer)
            .build());
            }
            return out;
        } catch (Exception ex) {
            // fallback: try wrapper object with field 'questions'
            Map<String, Object> wrapper = xml.readValue(file, Map.class);
            Object maybe = wrapper.get("questions");
            if (maybe instanceof List) {
                List<Map<String, Object>> arr = (List<Map<String, Object>>) maybe;
                for (Map<String, Object> m : arr) {
                    String category = String.valueOf(m.getOrDefault("category", m.getOrDefault("Category", "")));
                    int value = 0; try { value = Integer.parseInt(String.valueOf(m.getOrDefault("value", m.getOrDefault("Value", "0")))); } catch (Exception ignored) {}
                    String text = String.valueOf(m.getOrDefault("question", m.getOrDefault("Question", "")));
                    String answer = String.valueOf(m.getOrDefault("answer", m.getOrDefault("Answer", "")));
            out.add(new com.jeopardy.model.QuestionBuilder()
                .category(category)
                .value(value)
                .text(text)
                .answer(answer)
                .build());
                }
            }
        }
        return out;
    }