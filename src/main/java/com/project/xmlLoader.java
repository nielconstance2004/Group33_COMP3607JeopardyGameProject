package com.project;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class xmlLoader implements Loader{
    
    public void load(){

        Question[] questions = new Question[30];
        int index=0;
         try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("/workspaces/COMP3607JeopardyGameProject/src/main/java/com/project/sample_game_XML.xml");
            NodeList nodeList = document.getElementsByTagName("QuestionItem");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                  //System.out.println(i);

               if (node.getNodeType() == Node.ELEMENT_NODE) {
                 Element element = (Element) node;

                 questions[index++]= new Question(element.getElementsByTagName("Category").item(0).getTextContent(),
                                    Integer.parseInt(element.getElementsByTagName("Value").item(0).getTextContent()),
                                     element.getElementsByTagName("QuestionText").item(0).getTextContent(),
                                     element.getElementsByTagName("OptionA").item(0).getTextContent(),
                                     element.getElementsByTagName("OptionB").item(0).getTextContent(),
                                     element.getElementsByTagName("OptionC").item(0).getTextContent(),
                                     element.getElementsByTagName("OptionD").item(0).getTextContent(),
                                     element.getElementsByTagName("CorrectAnswer").item(0).getTextContent());
              //  System.out.println(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Question q : questions) {
            if (q != null) {
                System.out.println(q.toString());
            }
        }
    }
}
