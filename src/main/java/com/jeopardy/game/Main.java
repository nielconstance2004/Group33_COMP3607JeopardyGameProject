package com.jeopardy.game;

import com.jeopardy.engine.EventLogger;
import com.jeopardy.engine.GameEngine;
import com.jeopardy.model.Player;
import com.jeopardy.report.ReportGenerator;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Jeopardy Game - Console Version");

            // Set up log
            String base = System.getProperty("user.dir");
            File logFile = new File(base, "game_event_log.csv");
            EventLogger logger = EventLogger.getInstance(logFile);

            // Set up engine
            GameEngine engine = GameEngine.getInstance();
            engine.setLogger(logger);
            logEvent(logger, engine, "SYSTEM", "LAUNCH", "Main started");

            // Load question file
            System.out.print("Enter question data file path (CSV/JSON/XML): ");
            String path = scanner.nextLine().trim();
            File questionFile = new File(path);
            if (!questionFile.exists()) {
                System.out.println("File not found: " + path);
                return;
            }
            engine.loadQuestions(questionFile);

            // Get players
            System.out.print("Number of players (1-4): ");
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n < 1 || n > 4) {
                System.out.println("Invalid player count. Must be 1-4.");
                return;
            }
            logEvent(logger, engine, "SYSTEM", "SELECT_PLAYER_COUNT", String.valueOf(n));

            for (int i = 1; i <= n; i++) {
                System.out.print("Player " + i + " name: ");
                String name = scanner.nextLine().trim();
                engine.addPlayerWithLimit(new Player("P" + i, name));
                logEvent(logger, engine, "SYSTEM", "ENTER_PLAYER_NAME", name);
            }

            // Run the game
            engine.runConsoleGame();

            // Generate report
            File report = new File(base, "sample_game_report.txt");
            logEvent(logger, engine, "SYSTEM", "GENERATE_REPORT", report.getName() + " START");
            ReportGenerator rg = new ReportGenerator();
            rg.generateTxtReport(report, engine.getPlayers(), engine.getRecords(), logger);
            logEvent(logger, engine, "SYSTEM", "GENERATE_REPORT", report.getName() + " OK");

            System.out.println("Report written to: " + report.getAbsolutePath());
            System.out.println("Log written to: " + logFile.getAbsolutePath());

            logEvent(logger, engine, "SYSTEM", "EXIT_GAME", "User exit");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void logEvent(EventLogger logger, GameEngine engine, String actor, String action, String details) {
        if (logger != null) {
            logger.log(engine.getCaseId(), actor, action, "", null, details, "OK", 0);
        }
    }
}
