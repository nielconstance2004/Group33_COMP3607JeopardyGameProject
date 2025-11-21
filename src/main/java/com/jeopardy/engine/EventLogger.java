package com.jeopardy.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventLogger {
    private static EventLogger instance;
    private final File outFile;
    private boolean headerWritten = false;

    private EventLogger(File outFile) throws Exception {
        this.outFile = outFile;
        if (!outFile.exists()) outFile.createNewFile();
    }

    public static synchronized EventLogger getInstance(File outFile) throws Exception {
        if (instance == null) instance = new EventLogger(outFile);
        return instance;
    }

    public synchronized void log(String caseId, String playerId, String activity, String category, Integer questionValue,
                                 String answerGiven, String result, int scoreAfter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true))) {
            if (!headerWritten && outFile.length() == 0) {
                bw.write("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play\n");
                headerWritten = true;
            }
            String ts = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%d\n",
                    safe(caseId), safe(playerId), safe(activity), safe(ts), safe(category),
                    questionValue == null ? "" : questionValue.toString(), safe(answerGiven), safe(result), scoreAfter);
            bw.write(line);
        } catch (Exception ex) {
            System.err.println("Failed to write log: " + ex.getMessage());
        }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replaceAll(",", " ").replaceAll("\n", " ").replaceAll("\r", " ");
    }
}
