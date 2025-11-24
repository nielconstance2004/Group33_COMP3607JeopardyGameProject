package com.jeopardy.test;

import org.junit.jupiter.api.*;
import com.jeopardy.model.Player;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTests{
    private GameEngine instance;

    @BeforeEach
    void initialize() {
        instance = GameEngine.getInstance();
        instance.getPlayers().clear();
    }

    @Test
    void addPlayerToLimit() {
        for (int i = 1; i <= 4; i++) 
        instance.addPlayerWithLimit(new Player("P" + i, "Player" + i));
        assertThrows(IllegalStateException.class,
             () -> instance.addPlayerWithLimit(new Player("P5", "Player5"))
        );
    }

    @Test
    void testCategoriesAndValuesEmpty() {
        assertTrue(instance.categories().isEmpty());
    }

    @Test
    void testAllAnsweredEmptyBoard() {
        assertTrue(instance.allAnswered());
    }

    @Test
    void testLoadQuestionsUnsupportedFile() {
        File bad = new File("bad.xxx");
        assertThrows(IllegalArgumentException.class, () -> instance.loadQuestions(bad));
    }
}