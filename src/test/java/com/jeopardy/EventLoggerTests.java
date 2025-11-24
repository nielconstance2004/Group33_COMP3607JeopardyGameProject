package com.jeopardy.test;

import org.junit.jupiter.api.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventLoggerTests{
    private File outFile;

    @BeforeEach
    void initialize() throws Exception{
        outFile = File.createTempFile("eventlog", ".csv");
        outFile.deleteOnExit();
    }

    @Test
    void testSingletonBehavior() throws Exception {
        EventLogger logger1 = EventLogger.getInstance(outFile);
        EventLogger logger2 = EventLogger.getInstance(outFile);
        assertSame(logger1, logger2);
    }

    @Test
    void testLogWritesLine() throws Exception {
        EventLogger logger = EventLogger.getInstance(outFile);
        logger.log("CASE1", "PLAYER1", "TEST_ACTIVITY", "CATEGORY", 100, "ANSWER", "RESULT", 50);

        List<String> lines = Files.readAllLines(outFile.toPath());
        assertEquals(2, lines.size()); // header + one log
        assertTrue(lines.get(1).contains("CASE1"));
        assertTrue(lines.get(1).contains("PLAYER1"));
        assertTrue(lines.get(1).contains("TEST_ACTIVITY"));
    }



}