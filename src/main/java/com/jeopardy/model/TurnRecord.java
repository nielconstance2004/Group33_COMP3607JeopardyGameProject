package com.jeopardy.model;

import java.time.ZonedDateTime;

public class TurnRecord {
    private final String playerId;
    private final String playerName;
    private final String category;
    private final int value;
    private final String questionText;
    private final String answerGiven;
    private final boolean correct;
    private final int pointsEarned;
    private final int runningTotal;
    private final ZonedDateTime timestamp;

    public TurnRecord(String playerId, String playerName, String category, int value, String questionText,
                      String answerGiven, boolean correct, int pointsEarned, int runningTotal, ZonedDateTime timestamp) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.category = category;
        this.value = value;
        this.questionText = questionText;
        this.answerGiven = answerGiven;
        this.correct = correct;
        this.pointsEarned = pointsEarned;
        this.runningTotal = runningTotal;
        this.timestamp = timestamp;
    }

    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public String getCategory() { return category; }
    public int getValue() { return value; }
    public String getQuestionText() { return questionText; }
    public String getAnswerGiven() { return answerGiven; }
    public boolean isCorrect() { return correct; }
    public int getPointsEarned() { return pointsEarned; }
    public int getRunningTotal() { return runningTotal; }
    public ZonedDateTime getTimestamp() { return timestamp; }
}
