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
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Manages questions and history.
 * Handles interaction with JSON files: question.json, history.json
 */
public class SysData {
    private static SysData instance = null;
    private ArrayList<Question> questionList;
    private static final String QUESTIONS_FILE = "questions.json";
    private static final String HISTORY_FILE = "GameHistory.json";

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
        saveQuestionsToJsonFile();
    }


    public void addGameHistory(String winnerName, String gameDuration, String difficulty) {
        JSONObject jsonObject = readJsonFile(HISTORY_FILE);
        JSONArray gameHistoryArray = jsonObject.optJSONArray("GameHistory");
        if (gameHistoryArray == null) {
            gameHistoryArray = new JSONArray();
            jsonObject.put("GameHistory", gameHistoryArray);
        }

        JSONObject newGameHistoryJson = new JSONObject();
        newGameHistoryJson.put("winner", winnerName);
        newGameHistoryJson.put("duration", gameDuration);  // Duration as a formatted string
        newGameHistoryJson.put("difficulty", difficulty);

        gameHistoryArray.put(newGameHistoryJson);
        saveJsonToFile(jsonObject, HISTORY_FILE);
    }

    public String calculateGameDuration(long startTimeMillis) {
        long durationMillis = System.currentTimeMillis() - startTimeMillis;
        long seconds = (durationMillis / 1000) % 60;
        long minutes = (durationMillis / (1000 * 60)) % 60;
        long hours = (durationMillis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }




    /**
     * Imports history from JSON file.
     * @return List of game history
     */
    private ArrayList<GameHistory> importHistory() {
        ArrayList<GameHistory> historyList = new ArrayList<>();
        JSONObject jsonObject = readJsonFile(HISTORY_FILE); // Ensure this method is implemented to read your JSON file

        // Access the "GameHistory" array from the JSON object
        JSONArray historyArray = jsonObject.optJSONArray("GameHistory");
        if (historyArray != null) {
            for (Object obj : historyArray) {
                JSONObject historyJson = (JSONObject) obj;
                // Create a new GameHistory object with the winner, duration, and difficulty
                GameHistory gameHistory = new GameHistory(
                        historyJson.getString("winner"),
                        historyJson.getString("duration"),
                        historyJson.getString("difficulty")
                );
                historyList.add(gameHistory);
            }
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
        int correctAns = questionJson.getInt("correct_ans");
        int difficulty = questionJson.getInt("difficulty");
        return new Question(questionText, answers, correctAns, difficulty);
    }


    private JSONObject createQuestionJson(Question question) {
        JSONObject questionJson = new JSONObject();
        questionJson.put("question", question.getQuestionText());
        questionJson.put("correct_ans", question.getCorrectAns());
        questionJson.put("difficulty", question.getDifficulty());
        JSONArray answersArray = new JSONArray();
        question.getAnswers().values().forEach(answersArray::put);
        questionJson.put("answers", answersArray);
        return questionJson;
    }

    private JSONObject readJsonFile(String filename) {
        try (InputStream stream = getClass().getResourceAsStream("/" + filename)) {
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
        // Logic to remove the question
        questionList.removeIf(q -> q.getQuestionText().equals(questionText));
        // Save the updated list to JSON file
        saveQuestionsToJsonFile();
    }

    public void updateQuestion(Question updatedQuestion) {
        // Logic to update the question
        boolean found = false;
        for (int i = 0; i < questionList.size(); i++) {
            if (questionList.get(i).getQuestionText().equals(updatedQuestion.getQuestionText())) {
                questionList.set(i, updatedQuestion);
                found = true;
                break;
            }
        }

        if (found) {
            saveQuestionsToJsonFile();
        } else {
            System.out.println("msh mlaqe");
        }
    }
    public void saveQuestionsToJsonFile() {
        JSONObject jsonObject = new JSONObject();
        JSONArray questionsArray = new JSONArray();
        for (Question q : questionList) {
            questionsArray.put(createQuestionJson(q));
        }
        jsonObject.put("questions", questionsArray);

        // Save to file
        String relativePath = "src/main/resources/" + QUESTIONS_FILE; // Adjust this path as needed
        try (FileWriter file = new FileWriter(relativePath)) {
            file.write(jsonObject.toString(4)); // Indentation for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // In SysData
    public Question getRandomQuestion(int difficulty) {
        List<Question> filteredQuestions = questionList.stream()
                .filter(q -> q.getDifficulty() == difficulty)
                .collect(Collectors.toList());
        if (filteredQuestions.isEmpty()) {
            return null;
        }
        return filteredQuestions.get(new Random().nextInt(filteredQuestions.size()));
    }

    /**
     * Saves a JSON object to a file.
     * @param jsonObject The JSON object to save
     * @param filename The filename to save to
     */
    private void saveJsonToFile(JSONObject jsonObject, String filename) {
        String relativePath = "src/main/resources/" + filename; // Adjust this path as needed
        try (FileWriter file = new FileWriter(relativePath)) {
            file.write(jsonObject.toString(4)); // Indentation for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // Make sure history is imported and cached
        ArrayList<GameHistory> history = importHistory();
        return "SysData{" +
                "questions=" + questionList +
                ", history=" + history +
                '}';
    }

}
