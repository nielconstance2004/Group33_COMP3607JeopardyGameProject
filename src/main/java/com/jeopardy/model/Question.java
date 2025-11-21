package com.jeopardy.model;

public class Question {
    private String id;
    private String category;
    private int value;
    private String text;
    private String answer;
    private boolean asked = false;

    public Question() {}

    public Question(String id, String category, int value, String text, String answer) {
        this.id = id;
        this.category = category;
        this.value = value;
        this.text = text;
        this.answer = answer;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public boolean isAsked() { return asked; }
    public void setAsked(boolean asked) { this.asked = asked; }
}
