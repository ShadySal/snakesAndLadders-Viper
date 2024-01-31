package com.example.snakesandladdersviper.Model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages questions and history.
 * Handles interaction with JSON files: question.json, history.json
 */
public class SysData {
    private static SysData instance = null;
    private ArrayList<Question> questionList;
    private static final String QUESTIONS_FILE = "questions.json";
    private static final String HISTORY_FILE = "history.json";

    /**
     * Singleton pattern to ensure only one instance of SysData.
     * @return SysData instance
     */
    public static SysData getInstance() {
        if (instance == null) {
            instance = new SysData();
        }
        return instance;
    }

    /**
     * Initializes questions list and imports questions from JSON.
     */
    private SysData() {
        questionList = new ArrayList<>();
        importQuestions();
    }

    public ArrayList<Question> getQuestions() {
        return questionList;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questionList = questions;
    }

    public ArrayList<GameHistory> getHistory() {
        return importHistory();
    }

    /**
     * Adds a question and updates JSON file.
     * @param question The question to add
     */
    public void addQuestion(Question question) {
        JSONObject jsonObject = readJsonFile(QUESTIONS_FILE);
        JSONArray questionsArray = jsonObject.getJSONArray("questions");

        JSONObject newQuestionJson = createQuestionJson(question);
        questionsArray.put(newQuestionJson);

        questionList.add(question);
        saveJsonToFile(jsonObject, QUESTIONS_FILE);
    }

    /**
     * Adds a high score entry to the JSON file.
     * @param playerName The player's name
     * @param score The player's score
     */
    public void addHighScore(String playerName, int score) {
        JSONObject jsonObject = readJsonFile(HISTORY_FILE);
        JSONArray historyArray = jsonObject.getJSONArray("history");

        JSONObject newScoreJson = new JSONObject();
        newScoreJson.put("player", playerName);
        newScoreJson.put("score", score);
        newScoreJson.put("date", LocalDate.now());

        historyArray.put(newScoreJson);
        saveJsonToFile(jsonObject, HISTORY_FILE);
    }

    /**
     * Imports history from JSON file.
     * @return List of game history
     */
    private ArrayList<GameHistory> importHistory() {
        ArrayList<GameHistory> historyList = new ArrayList<>();
        JSONObject jsonObject = readJsonFile(HISTORY_FILE);

        JSONArray historyArray = jsonObject.getJSONArray("history");
        for (Object obj : historyArray) {
            JSONObject historyJson = (JSONObject) obj;
            GameHistory gameHistory = new GameHistory(
                    historyJson.getString("player"),
                    historyJson.getInt("score"),
                    parseDate(historyJson.getString("date"))
            );
            historyList.add(gameHistory);
        }
        return historyList;
    }

    private LocalDate parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Imports questions from JSON file.
     */
    private void importQuestions() {
        JSONObject jsonObject = readJsonFile(QUESTIONS_FILE);
        JSONArray questionsArray = jsonObject.getJSONArray("questions");

        for (Object obj : questionsArray) {
            JSONObject questionJson = (JSONObject) obj;
            Question question = parseQuestionJson(questionJson);
            questionList.add(question);
        }
    }

    private Question parseQuestionJson(JSONObject questionJson) {
        String questionText = questionJson.getString("question");
        JSONArray answersJsonArray = questionJson.getJSONArray("answers");
        HashMap<Integer, String> answers = new HashMap<>();
        for (int i = 0; i < answersJsonArray.length(); i++) {
            answers.put(i + 1, answersJsonArray.getString(i));
        }
        return new Question(
                questionText,
                answers,
                questionJson.getInt("correct_ans"),
                questionJson.getInt("level"),
                questionJson.getString("team")
        );
    }

    private JSONObject createQuestionJson(Question question) {
        JSONObject questionJson = new JSONObject();
        questionJson.put("question", question.getQuestionID());
        questionJson.put("level", question.getLevel());
        JSONArray answersArray = new JSONArray(question.getAnswers().values());
        questionJson.put("answers", answersArray);
        questionJson.put("team", question.getTeam());
        questionJson.put("correct_ans", question.getCorrect_ans());
        return questionJson;
    }

    private JSONObject readJsonFile(String filename) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (stream == null) {
                throw new FileNotFoundException("Resource not found: " + filename);
            }
            return new JSONObject(new JSONTokener(stream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Removes a question and updates the JSON file.
     * @param questionText The question to remove
     */
    public void removeQuestion(String questionText) {
        JSONObject jsonObject = readJsonFile(QUESTIONS_FILE);
        JSONArray questionsArray = jsonObject.getJSONArray("questions");
        int indexToRemove = -1;

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionJson = questionsArray.getJSONObject(i);
            if (questionJson.getString("question").equals(questionText)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove >= 0) {
            questionsArray.remove(indexToRemove);
            questionList.removeIf(q -> q.getQuestionID().equals(questionText));
            saveJsonToFile(jsonObject, QUESTIONS_FILE);
        }
    }

    /**
     * Saves a JSON object to a file.
     * @param jsonObject The JSON object to save
     * @param filename The filename to save to
     */
    private void saveJsonToFile(JSONObject jsonObject, String filename) {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(jsonObject.toString(4)); // Indentation for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "SysData{" +
                "questions=" + questionList +
                ", history=" + getHistory() +
                '}';
    }
}
