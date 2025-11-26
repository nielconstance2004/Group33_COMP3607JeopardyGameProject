package com.jeopardy.loaders;

import com.jeopardy.model.Question; // question model
import java.io.BufferedReader; // for reading files
import java.io.File; // for file representation
import java.io.FileReader; // for reading character files
import java.util.ArrayList; // for list implementation
import java.util.List; // for list interface
 
// CSV question loader implementation
public class CsvQuestionLoader implements QuestionLoader { // implement QuestionLoader interface
    @Override // override load method
    public List<Question> load(File file) throws Exception { // load questions from file
        List<Question> list = new ArrayList<>(); // initialize output list
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // open file for reading
            String line; // variable for each line
            boolean headerSkipped = false; // flag to skip header
            while ((line = br.readLine()) != null) { // read each line
                line = line.trim(); // trim whitespace
                if (line.isEmpty()) continue; // skip empty lines
                    // split CSV line while respecting quoted fields
                    java.util.List<String> partsList = splitCsvLine(line);
                    String[] parts = partsList.toArray(new String[0]); // split CSV line
                // naive: allow header detection
                if (!headerSkipped) { // skip header if present
                    boolean looksLikeHeader = line.toLowerCase().contains("category") || line.toLowerCase().contains("question"); // check for header keywords
                    if (looksLikeHeader) { headerSkipped = true; continue; } // skip header line
                    headerSkipped = true; // set flag after first line
                } // end header skip check
                // Expecting at least: category,value,question,answer
                if (parts.length < 4) continue; // skip invalid lines
                String category = parts[0].trim().replaceAll("\"", ""); // get category
                int value = 0; // initialize value
                try { value = Integer.parseInt(parts[1].trim()); } catch (Exception ex) { value = 0; } // parse value
                 String questionText = parts[2].trim().replaceAll("\"", ""); // get question text
                // optional choices in CSV: choiceA,choiceB,choiceC,choiceD (columns 4..7)
                String a = parts.length > 3 ? parts[3].trim().replaceAll("\"", "") : null; // get choice A
                String b = parts.length > 4 ? parts[4].trim().replaceAll("\"", "") : null; // get choice B
                String c = parts.length > 5 ? parts[5].trim().replaceAll("\"", "") : null; // get choice C
                String d = parts.length > 6 ? parts[6].trim().replaceAll("\"", "") : null; // get choice D
                String answer = parts[7].trim().replaceAll("\"", ""); // get answer
        list.add(new com.jeopardy.model.QuestionBuilder() // build question
            .category(category) // set category
            .value(value) // set value
            .text(questionText) // set question text
            .answer(answer) // set answer
            .choiceA(a) // set choice A
            .choiceB(b) // set choice B
            .choiceC(c) // set choice C
            .choiceD(d) // set choice D
            .build()); // build and add to list
            } // end while
        } // end try
        return list; // return list of questions
    } // end load method
        // Simple CSV line splitter that respects double-quoted fields and escaped quotes
        private java.util.List<String> splitCsvLine(String line) {
            java.util.List<String> out = new java.util.ArrayList<>();
            if (line == null || line.isEmpty()) return out;
            StringBuilder cur = new StringBuilder();
            boolean inQuotes = false;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '"') {
                    if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        // escaped quote
                        cur.append('"');
                        i++; // skip escaped quote
                    } else {
                        inQuotes = !inQuotes;
                    }
                } else if (c == ',' && !inQuotes) {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else {
                    cur.append(c);
                }
            }
            out.add(cur.toString());
            return out;
        }

} // end class
