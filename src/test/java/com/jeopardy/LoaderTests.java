package com.jeopardy.test;

import com.jeopardy.model.Question;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoaderTests {

    private File csv;
    private File json;
    private File xml;

    @BeforeEach
    void initialize() throws Exception {

        // --- CSV TEST FILE ---
        csv = File.createTempFile("questions", ".csv");
        try (FileWriter fw = new FileWriter(csv)) {
            fw.write("Category,Value,Question,Answer\n");
            fw.write("Control Structures,100,Which statement is used to make a decision?,A\n");
        }

        // --- JSON TEST FILE ---
        json = File.createTempFile("questions", ".json");
        try (FileWriter fw = new FileWriter(json)) {
            fw.write("[{\"category\":\"Variables & Data Types\",\"value\":100,");
            fw.write("\"question\":\"Which declares an int variable in C++?\",\"answer\":\"A\"}]");
        }

        // --- XML TEST FILE ---
        xml = File.createTempFile("questions", ".xml");
        try (FileWriter fw = new FileWriter(xml)) {
            fw.write("<questions>");
            fw.write("<question>");
            fw.write("<category>Variables</category>");
            fw.write("<value>100</value>");
            fw.write("<question>Which declares an int variable in C++?</question>");
            fw.write("<answer>A</answer>");
            fw.write("</question>");
            fw.write("</questions>");
        }
    }

    @Test
    void testCsvLoader() throws Exception {
        CsvQuestionLoader loader = new CsvQuestionLoader();
        List<Question> list = loader.load(csv);

        assertEquals(1, list.size());
        assertEquals("Control Structures", list.get(0).getCategory());
        assertEquals(100, list.get(0).getValue());
    }

    @Test
    void testJsonLoader() throws Exception {
        JsonQuestionLoader loader = new JsonQuestionLoader();
        List<Question> list = loader.load(json);

        assertEquals(1, list.size());
        assertEquals("Variables & Data Types", list.get(0).getCategory());
    }

    @Test
    void testXmlLoader() throws Exception {
        XmlQuestionLoader loader = new XmlQuestionLoader();
        List<Question> list = loader.load(xml);

        assertEquals(1, list.size());
        assertEquals("Variables", list.get(0).getCategory());
    }

    @Test
    void testFactory() {
        assertTrue(QuestionLoaderFactory.getLoader(csv) instanceof CsvQuestionLoader);
        assertTrue(QuestionLoaderFactory.getLoader(json) instanceof JsonQuestionLoader);
        assertTrue(QuestionLoaderFactory.getLoader(xml) instanceof XmlQuestionLoader);
    }
}
