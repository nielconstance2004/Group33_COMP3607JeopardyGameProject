public class Question {

    private String Category;
    private int Value;
    private String Question;
    private String OptionA;
    private String OptionB;
    private String OptionC;
    private String OptionD;
    private String CorrectAns;

    public Question(String category, int value, String question, String optionA, String optionB, String optionC, String optionD, String correctAns) {
        this.Category = category;
        this.Value = value;
        this.Question = question;
        this.OptionA = optionA;
        this.OptionB = optionB;
        this.OptionC = optionC;
        this.OptionD = optionD;
        this.CorrectAns = correctAns;
    }

    public String getCategory() {
        return Category;
    }
    public int getValue() {
        return Value;
    }
    
    public String getQuestion() {
        return Question;
    }

    public String getOptionA() {
        return OptionA;
    }

    public String getOptionB() {
        return OptionB;
    }

    public String getOptionC() {
        return OptionC;
    }

    public String getOptionD() {
        return OptionD;
    }

    public String getCorrectAns() {
        return CorrectAns;
    }

    @Override
    public String toString() {
        return "Category: " + Category + "\n" +
               "Value: " + Value + "\n" +
               "Question: " + Question + "\n" +
               "Option A: " + OptionA + "\n" +
               "Option B: " + OptionB + "\n" +
               "Option C: " + OptionC + "\n" +
               "Option D: " + OptionD + "\n" +
               "Correct Answer: " + CorrectAns + "\n";
    }
    
}

