package com.project;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//fix it has commas in the file cant split by tht!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class csvloader implements Loader{

    public static List<String> parseCsvLine(String line) {//AI

        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim());

        return result;
    }

    public void load(){
    Question[] questions = new Question[30];
        int index = 0;

        String file = "/workspaces/COMP3607JeopardyGameProject/src/main/java/com/project/sample_game_CSV.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            reader.readLine(); 

            String line;
            while ((line = reader.readLine()) != null) {

                List<String> valuesList = parseCsvLine(line);
                String[] values = valuesList.toArray(new String[0]);

                if (values.length < 8) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                questions[index++] = new Question(
                        values[0],
                        Integer.parseInt(values[1]),
                        values[2],
                        values[3],
                        values[4],
                        values[5],
                        values[6],
                        values[7]
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Question q : questions) {
            if (q != null) {
                System.out.println(q);
            }
        }
    }
}
