package ch.srf.inte.quizfragen;

import ch.srf.inte.quizfragen.model.Difficulty;
import ch.srf.inte.quizfragen.model.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WwmQuestionJsonParser implements IQuestionParser {

    private static final Path FILE = Paths.get("src/main/resources/wwmQuestions.json");

    @Override
    public List<Question> getQuestions() throws IOException {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(FILE)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonArray prizeLevels = jsonElement.getAsJsonObject().getAsJsonArray("prizeLevels");
            for (int i = 0; i < prizeLevels.size(); i++) {
                Difficulty difficulty = getDifficulty(i, prizeLevels.size());
                JsonArray questionArray = prizeLevels.get(i).getAsJsonObject().getAsJsonArray("questions");
                questionArray.forEach(q -> questions.add(parseQuestion(q.getAsJsonObject(), difficulty)));
            }

        }
        return questions;
    }

    private Difficulty getDifficulty(int prizeLevelIndex, int totalPriceLevels) {
        double third = totalPriceLevels / 3.0;
        if (prizeLevelIndex + 1 < third) {
            return Difficulty.EASY;
        } else if (prizeLevelIndex + 1 < third * 2) {
            return Difficulty.MEDIUM;
        } else {
            return Difficulty.HARD;
        }
    }

    private Question parseQuestion(JsonObject jsonQuestion, Difficulty difficulty) {
        String question = jsonQuestion.get("question").getAsString();
        List<String> answers = List.of(jsonQuestion.get("answerA").getAsString(),
                                       jsonQuestion.get("answerB").getAsString(),
                                       jsonQuestion.get("answerC").getAsString(),
                                       jsonQuestion.get("answerD").getAsString());
        int answerIndex = answers.indexOf(jsonQuestion.get("correctAnswer").getAsString());
        return new Question(question, answers, answerIndex, difficulty);
    }
}
