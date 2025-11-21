package com.project;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class jsonLoader implements Loader {
     private String name;


    public jsonLoader(String name){
        this.name=name;
    }

    public String getName(){return this.name;}

    @Override
    public void load() {
    ObjectMapper objectMapper = new ObjectMapper();
    int i=0;
        try {

            JsonNode rootNode = objectMapper.readTree(new File("/workspaces/COMP3607JeopardyGameProject/src/main/java/com/project/sample_game_JSON.json"));
            Question[] questions = new Question[30];

            for (JsonNode node : rootNode) {
            String category = node.get("Category").asText();
            int value = node.get("Value").asInt();
            String questionText = node.get("Question").asText();
//ai
            JsonNode optionsNode = node.get("Options");
            String optionA = optionsNode != null && optionsNode.has("A") ? optionsNode.get("A").asText() : "";
            String optionB = optionsNode != null && optionsNode.has("B") ? optionsNode.get("B").asText() : "";
            String optionC = optionsNode != null && optionsNode.has("C") ? optionsNode.get("C").asText() : "";
            String optionD = optionsNode != null && optionsNode.has("D") ? optionsNode.get("D").asText() : "";
            String correctAns = node.has("CorrectAnswer") ? node.get("CorrectAnswer").asText() : "";

        Question qu = new Question(category, value, questionText, optionA, optionB, optionC, optionD, correctAns);
        questions[i]=qu;
        i++;
}  
 for (Question q : questions) {
            if (q != null) {
                System.out.println(q);
            } 
    } 
}
catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to read JSON file!");
        }
        
       
        }
    }
