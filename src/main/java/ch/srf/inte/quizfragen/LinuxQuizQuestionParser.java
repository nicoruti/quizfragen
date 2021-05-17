package ch.srf.inte.quizfragen;

import ch.srf.inte.quizfragen.model.Difficulty;
import ch.srf.inte.quizfragen.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LinuxQuizQuestionParser implements IQuestionParser {

    private static final Logger LOG = LoggerFactory.getLogger(LinuxQuizQuestionParser.class);
    private static final Path FILE = Paths.get("src/main/resources/linux-quiz.csv");

    @Override
    public List<Question> getQuestions() throws IOException {
        return Files.lines(FILE, StandardCharsets.UTF_8)
                    .filter(l -> !l.startsWith("#"))
                    .filter(l -> !l.isBlank())
                    .map(this::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }

    private Question parseLine(String line) {
        String[] split = line.split(";");
        if (split.length != 7) {
            LOG.error("Line has not 7 parts: {}", line);
            return null;
        }

        List<String> answers = List.of(split[1], split[2], split[3], split[4]);
        Difficulty difficulty = mapDifficulty(Integer.parseInt(split[6]));
        return new Question(split[0], answers, 0, difficulty);
    }

    private Difficulty mapDifficulty(int num) {
        if (num <= 1) {
            return Difficulty.EASY;
        } else if (num <= 3) {
            return Difficulty.MEDIUM;
        } else {
            return Difficulty.HARD;
        }
    }
}
