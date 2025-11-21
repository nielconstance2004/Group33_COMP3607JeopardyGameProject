package com.jeopardy.engine;

import com.jeopardy.loader.QuestionLoader;
import com.jeopardy.loader.QuestionLoaderFactory;
import com.jeopardy.model.Player;
import com.jeopardy.model.Question;
import com.jeopardy.model.TurnRecord;
import com.jeopardy.scoring.ScoringStrategy;
import com.jeopardy.scoring.SimpleScoringStrategy;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameEngine manages gameplay. Singleton for simplicity (demonstrates Singleton pattern).
 */
public class GameEngine {
    private static GameEngine instance;

    private final List<Player> players = new ArrayList<>();
    private final Map<String, Map<Integer, Question>> board = new LinkedHashMap<>();
    private final List<TurnRecord> records = new ArrayList<>();
    private ScoringStrategy scoringStrategy = new SimpleScoringStrategy(true);
    private EventLogger logger;
    private String caseId = UUID.randomUUID().toString();

    private GameEngine() {}

    public static synchronized GameEngine getInstance() {
        if (instance == null) instance = new GameEngine();
        return instance;
    }

    public void setLogger(EventLogger logger) { this.logger = logger; }
    public void setScoringStrategy(ScoringStrategy strategy) { this.scoringStrategy = strategy; }

    public void loadQuestions(File file) throws Exception {
        QuestionLoader loader = QuestionLoaderFactory.getLoader(file);
        if (loader == null) throw new IllegalArgumentException("Unsupported file type: " + file.getName());
        List<Question> list = loader.load(file);
        // populate board by category and value
        board.clear();
        for (Question q : list) {
            board.computeIfAbsent(q.getCategory(), k -> new TreeMap<>()).put(q.getValue(), q);
        }
        if (logger != null) logger.log(caseId, "SYSTEM", "LOAD_QUESTIONS", "", null, file.getName(), "OK", 0);
    }

    public void addPlayer(Player p) { players.add(p); if (logger != null) logger.log(caseId, p.getId(), "PLAYER_JOIN", "", null, p.getName(), "OK", p.getScore()); }

    /**
     * Adds a player but enforces the project requirement of maximum 4 players.
     * Throws IllegalStateException if trying to add more than 4 players.
     */
    public void addPlayerWithLimit(Player p) {
        if (p == null) throw new IllegalArgumentException("Player cannot be null");
        if (players.size() >= 4) {
            if (logger != null) logger.log(caseId, p.getId(), "PLAYER_JOIN_ATTEMPT", "", null, p.getName(), "FAILED_MAX_PLAYERS", players.stream().mapToInt(Player::getScore).sum());
            throw new IllegalStateException("Cannot add more than 4 players");
        }
        players.add(p);
        if (logger != null) logger.log(caseId, p.getId(), "PLAYER_JOIN", "", null, p.getName(), "OK", p.getScore());
    }

    public List<String> categories() { return new ArrayList<>(board.keySet()); }

    public List<Integer> valuesForCategory(String cat) {
        if (!board.containsKey(cat)) return Collections.emptyList();
        return new ArrayList<>(board.get(cat).keySet());
    }

    public boolean allAnswered() {
        for (Map<Integer, Question> m : board.values()) for (Question q : m.values()) if (!q.isAsked()) return false;
        return true;
    }

    public void runConsoleGame() {
        try (Scanner sc = new Scanner(System.in)) {
            if (players.isEmpty()) {
                System.out.println("No players added. Add players first.");
                return;
            }
            // Log start of game
            if (logger != null) logger.log(caseId, "SYSTEM", "START_GAME", "", null, "", "OK", players.stream().mapToInt(Player::getScore).sum());
            int current = 0;
            while (!allAnswered()) {
                Player p = players.get(current);
                System.out.println("\nPlayer: " + p.getName() + " (Score: " + p.getScore() + ")");
                System.out.println("Categories:");
                int i=1;
                List<String> cats = categories();
                for (String c : cats) System.out.println((i++)+". " + c);
                System.out.print("Choose category number or 'q' to quit: ");
                String catInput = sc.nextLine().trim();
                if (catInput.equalsIgnoreCase("q")) break;
                int cidx = -1;
                try { cidx = Integer.parseInt(catInput)-1; } catch (Exception ex) { System.out.println("Invalid"); continue; }
                if (cidx < 0 || cidx >= cats.size()) { System.out.println("Invalid"); continue; }
                String cat = cats.get(cidx);
                // Log category selection
                if (logger != null) logger.log(caseId, p.getId(), "SELECT_CATEGORY", cat, null, "", "OK", p.getScore());
                List<Integer> values = valuesForCategory(cat).stream().filter(v -> !board.get(cat).get(v).isAsked()).collect(Collectors.toList());
                if (values.isEmpty()) { System.out.println("No remaining questions in this category."); continue; }
                System.out.println("Available values: " + values);
                System.out.print("Choose value: ");
                String vStr = sc.nextLine().trim();
                int value = 0; try { value = Integer.parseInt(vStr); } catch (Exception ex) { System.out.println("Invalid"); continue; }
                if (!board.get(cat).containsKey(value)) { System.out.println("Invalid"); continue; }
                Question q = board.get(cat).get(value);
                if (q.isAsked()) { System.out.println("Already asked"); continue; }

                // Log question selection
                if (logger != null) logger.log(caseId, p.getId(), "SELECT_QUESTION", cat, value, "", "OK", p.getScore());

                System.out.println("Question: " + q.getText());
                System.out.print("Your answer: ");
                String ans = sc.nextLine().trim();

                // Use the user's raw answer for scoring (comparisons use Question.getAnswer())
                String normalizedForScoring = ans;

                // call scoring with normalizedForScoring (preserves existing ScoringStrategy signature)
                int delta = scoringStrategy.scoreForAnswer(p, q, normalizedForScoring);
                boolean correct = delta > 0;
                p.addScore(delta);
                q.setAsked(true);

                // Log score update
                if (logger != null) logger.log(caseId, p.getId(), "SCORE_UPDATED", cat, value, ans, correct ? "CORRECT" : "WRONG", p.getScore());

                TurnRecord tr = new TurnRecord(p.getId(), p.getName(), cat, value, q.getText(), ans, correct, delta, p.getScore(), ZonedDateTime.now());
                records.add(tr);
                if (logger != null) logger.log(caseId, p.getId(), "ANSWER", cat, value, ans, correct ? "CORRECT" : "WRONG", p.getScore());

                System.out.println((correct?"Correct!":"Wrong.") + " Points: " + delta + " New total: " + p.getScore());

                current = (current + 1) % players.size();
            }
            if (logger != null) logger.log(caseId, "SYSTEM", "END_GAME", "", null, "", "OK", players.stream().mapToInt(Player::getScore).sum());
            System.out.println("Game over. Generating report...");
        }
    }

    public List<TurnRecord> getRecords() { return records; }
    public List<Player> getPlayers() { return players; }
    public String getCaseId() { return caseId; }
}
