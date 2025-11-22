package com.jeopardy.report;

import com.jeopardy.engine.EventLogger;
import com.jeopardy.model.TurnRecord;
import com.jeopardy.model.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ReportGenerator {
    public void generateTxtReport(File outFile, List<Player> players, List<TurnRecord> records) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
            bw.write("Multi-Player Jeopardy - Summary Report\n");
            bw.write("==============================\n\n");
            bw.write("Final Scores:\n");
            for (Player p : players) bw.write(String.format("%s: %d\n", p.getName(), p.getScore()));
            bw.write("\nTurn-by-turn:\n");
            for (TurnRecord t : records) {
                bw.write(String.format("Player: %s (%s) | Category: %s | Value: %d\n", t.getPlayerName(), t.getPlayerId(), t.getCategory(), t.getValue()));
                bw.write(String.format("Question: %s\n", t.getQuestionText()));
                bw.write(String.format("Answer Given: %s | Correct: %s | Points Earned: %d | Running Total: %d\n\n",
                        t.getAnswerGiven(), t.isCorrect() ? "YES" : "NO", t.getPointsEarned(), t.getRunningTotal()));
            }
        }
    }

    /**
     * Overloaded report generator that logs report generation events via EventLogger when provided.
     */
    public void generateTxtReport(File outFile, List<Player> players, List<TurnRecord> records, EventLogger logger) throws Exception {
        if (logger != null) logger.log("", "SYSTEM", "GENERATE_REPORT", "", null, outFile.getName(), "START", 0);
        generateTxtReport(outFile, players, records);
        if (logger != null) logger.log("", "SYSTEM", "GENERATE_REPORT", "", null, outFile.getName(), "OK", 0);
    }
}
