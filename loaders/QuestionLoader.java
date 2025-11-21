package com.jeopardy.loader;
import com.jeopardy.model.Question;
import java.io.File;
import java.util.List;

public interface QuestionLoader {
    List<Question> load(File file) throws Exception;
}

