package com.jeopardy.model;

import java.util.UUID;

public class QuestionBuilder {
    private String id = UUID.randomUUID().toString();
    private String category = "";
    private int value = 0;
    private String text = "";
    private String answer = "";

    public QuestionBuilder id(String id) { this.id = id; return this; }
    public QuestionBuilder category(String category) { this.category = category; return this; }
    public QuestionBuilder value(int value) { this.value = value; return this; }
    public QuestionBuilder text(String text) { this.text = text; return this; }
    public QuestionBuilder answer(String answer) { this.answer = answer; return this; }

    public Question build() {
        return new Question(id, category, value, text, answer);
    }
}
