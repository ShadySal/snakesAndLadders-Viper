package com.example.snakesandladdersviper;
import java.util.HashMap;
/**
 * A class representing a question for the KnightMove game.
 * It includes the question's level, text, and answer.
 */
public class Question {
    private String questionID;
    private HashMap<Integer, String> answers;
    private int correct_ans;
    private int level;
    private String team;
    /**
     * Construct a new Question with the given level, text, and answer.
     * @param level the level of difficulty of the question
     * @param questionID the text of the question
     * @param correct_ans the correct answer to the question
     * @param answers possible answers
     * @param team witch team wrote the question
     */
    public Question(String questionID, HashMap<Integer, String> answers, int correct_ans, int level, String team) {
        this.questionID = questionID;
        this.answers = answers;
        this.correct_ans = correct_ans;
        this.level = level;
        this.team = team;
    }

    public Question(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public HashMap<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Integer, String> answers) {
        this.answers = answers;
    }

    public int getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(int correct_ans) {
        this.correct_ans = correct_ans;
    }

    public int getLevel() {
        return level;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return questionID.equals(question.questionID);
    }

    @Override
    public int hashCode() {
        return questionID.hashCode();
    }

    @Override
    public String toString() {
        return questionID;
    }
}
