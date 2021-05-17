package ch.srf.inte.quizfragen;

import ch.srf.inte.quizfragen.model.Question;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppStarter {

    private static final Path OUT_FILE = Paths.get("src/main/resources/aggregated.json");

    public static void main(String[] args) throws IOException {
        List<IQuestionParser> parsers = List.of(
                new LinuxQuizQuestionParser(),
                new Wwm2QuestionParser(),
                new OotQuestionParser(),
                new WwmQuestionJsonParser()
        );

        List<Question> questions = new ArrayList<>();
        for (IQuestionParser parser : parsers) {
            questions.addAll(parser.getQuestions());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(OUT_FILE);) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(questions, writer);
        }
    }
}
