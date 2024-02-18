

package com.example.snakesandladdersviper.test;

import com.example.snakesandladdersviper.Model.Question;
import com.example.snakesandladdersviper.Model.SysData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class sysDataTest {
    @Test
    void testSysDataImportQuestionsNotNull() {
        SysData sysData = SysData.getInstance();
        ArrayList<Question> question = sysData.getQuestions();
        Assertions.assertTrue(question.size()>0);

    }

    @Test
    void testAddAndRemoveFromJson() {
        //initializing sysData
        SysData sysData = SysData.getInstance();
        HashMap<Integer,String> testAnswers = new HashMap<>();
        testAnswers.put(1,"test1");
        testAnswers.put(2,"test2");
        testAnswers.put(3,"test3");
        testAnswers.put(4,"test4");
        //creating new question to add to json
        Question question = new Question("Does this question appear in the Json?",testAnswers,1,1);
        ArrayList<Question> testQuestions;
        sysData.addQuestion(question);
        testQuestions = sysData.getQuestions();
        System.out.println(testQuestions);
        Assertions.assertTrue(testQuestions.contains(question));
        sysData.removeQuestion("Does this question appear in the Json?");
        Assertions.assertFalse(sysData.getQuestions().contains(question));
    }
}


