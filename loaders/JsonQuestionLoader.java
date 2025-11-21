package com.jeopardy.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeopardy.model.Question;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 

public class JsonQuestionLoader implements QuestionLoader {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Question> load(File file) throws Exception {
        List<Question> out = new ArrayList<>();
        // Expect either array of objects with fields: category, value, question, answer OR map
        List<Map<String, Object>> arr = mapper.readValue(file, new TypeReference<List<Map<String, Object>>>(){});
        for (Map<String, Object> m : arr) {
            String category = String.valueOf(m.getOrDefault("category", m.getOrDefault("Category", "")));
            int value = 0;
            try { value = Integer.parseInt(String.valueOf(m.getOrDefault("value", m.getOrDefault("Value", "0")))); } catch (Exception ignored) {}
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
    }
}

