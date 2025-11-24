package com.jeopardy;

import com.jeopardy.engine.EventLogger;
import com.jeopardy.engine.GameEngine;
import com.jeopardy.model.Player;
import com.jeopardy.report.ReportGenerator;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Jeopardy Game - Console version");
        String base = System.getProperty("user.dir");
        File logFile = new File(base, "game_event_log.csv");
        EventLogger logger = EventLogger.getInstance(logFile);

    GameEngine engine = GameEngine.getInstance();
    engine.setLogger(logger);

    // Validate and log start
    if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "LAUNCH", "", null, "Main started", "OK", 0);

        try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
            System.out.print("Enter question data file path (CSV/JSON/XML): ");
            String path = sc.nextLine().trim();
        File data = new File(path);
        if (!data.exists()) { System.out.println("File not found: " + path); return; }
        engine.loadQuestions(data);

        System.out.print("Number of players (1-4): ");
            int n = Integer.parseInt(sc.nextLine().trim());
            if (n < 1 || n > 4) { System.out.println("Invalid player count. Must be 1-4."); return; }
            if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "SELECT_PLAYER_COUNT", "", null, String.valueOf(n), "OK", 0);
            for (int i=1;i<=n;i++) {
                System.out.print("Player " + i + " name: ");
                String name = sc.nextLine().trim();
                // enforce project requirement of max 4 players
                engine.addPlayerWithLimit(new Player("P"+i, name));
                if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "ENTER_PLAYER_NAME", "", null, name, "OK", 0);
            }

            engine.runConsoleGame();
        }

        ReportGenerator rg = new ReportGenerator();
        File report = new File(base, "sample_game_report.txt");
        // Use overloaded report generator that also logs events (Main also logs for clarity)
        if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "GENERATE_REPORT", "", null, report.getName(), "START", 0);
        rg.generateTxtReport(report, engine.getPlayers(), engine.getRecords(), logger);
        if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "GENERATE_REPORT", "", null, report.getName(), "OK", 0);
        if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "GENERATE_EVENT_LOG", "", null, logFile.getName(), "OK", 0);

        System.out.println("Report written to: " + report.getAbsolutePath());
        System.out.println("Log written to: " + logFile.getAbsolutePath());

        if (logger != null) logger.log(engine.getCaseId(), "SYSTEM", "EXIT_GAME", "", null, "User exit", "OK", engine.getPlayers().stream().mapToInt(p->p.getScore()).sum());
    }
}