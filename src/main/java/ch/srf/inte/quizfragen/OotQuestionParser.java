package ch.srf.inte.quizfragen;

import ch.srf.inte.quizfragen.model.Difficulty;
import ch.srf.inte.quizfragen.model.Question;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OotQuestionParser implements IQuestionParser {

    private static final Path FILE_EASY = Paths.get("src/main/resources/oot/questionsEasy.txt");
    private static final Path FILE_MEDIUM = Paths.get("src/main/resources/oot/questionsMedium.txt");
    private static final Path FILE_HARD = Paths.get("src/main/resources/oot/questionsHard.txt");

    @Override
    public List<Question> getQuestions() throws IOException {
        List<Question> questions = new ArrayList<>();
        questions.addAll(parseQuestions(FILE_EASY, Difficulty.EASY));
        questions.addAll(parseQuestions(FILE_MEDIUM, Difficulty.MEDIUM));
        questions.addAll(parseQuestions(FILE_HARD, Difficulty.HARD));
        return questions;
    }

    private List<Question> parseQuestions(Path file, Difficulty difficulty) throws IOException {
        List<Question> questions = new ArrayList<>();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()) {
            String question = iterator.next();
            String a = iterator.next().replace("A: ", "");
            String b = iterator.next().replace("B: ", "");
            String c = iterator.next().replace("C: ", "");
            String d = iterator.next().replace("D: ", "");
            int correctIndex = Integer.parseInt(iterator.next()) - 1;
            questions.add(new Question(question, List.of(a, b, c, d), correctIndex, difficulty));
        }
        return questions;
    }
}
