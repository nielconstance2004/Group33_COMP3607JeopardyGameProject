package com.jeopardy.test;

import com.jeopardy.model.Player;
import com.jeopardy.model.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoringTests {

    @Test
    void correctAnswer() {
        Player p = new Player("P1", "Bob");
        Question q = new Question.QuestionBuilder()
                .value(100)
                .answer("Answer")
                .build();

        SimpleScoringStrategy strategy = new SimpleScoringStrategy(true);

        int points = strategy.scoreForAnswer(p, q, "Answer");
        assertEquals(100, points);
    }

    @Test
    void wrongAnswerPenalty() {
        Player p = new Player("P1", "Bob");
        Question q = new Question.QuestionBuilder()
                .value(50)
                .answer("Answer")
                .build();

        SimpleScoringStrategy strategy = new SimpleScoringStrategy(true);

        int points = strategy.scoreForAnswer(p, q, "Wrong");
        assertEquals(-50, points);
    }

    @Test
    void wrongAnswerNoPenalty() {
        Player p = new Player("P1", "Bob");
        Question q = new Question.QuestionBuilder()
                .value(50)
                .answer("Answer")
                .build();

        // false → no penalty mode
        SimpleScoringStrategy strategy = new SimpleScoringStrategy(false);

        int points = strategy.scoreForAnswer(p, q, "Wrong");
        assertEquals(0, points);
    }
}
