package com.jeopardy.loader;
import java.io.File;

public class QuestionLoaderFactory {
    public static QuestionLoader getLoader(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".csv")) return new CsvQuestionLoader();
        if (name.endsWith(".json")) return new JsonQuestionLoader();
        if (name.endsWith(".xml")) return new XmlQuestionLoader();
        return null;
    }
}