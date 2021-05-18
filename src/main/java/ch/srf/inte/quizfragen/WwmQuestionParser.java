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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WwmQuestionParser implements IQuestionParser {

    private static final Logger LOG = LoggerFactory.getLogger(WwmQuestionParser.class);
    private static final Path FILE = Paths.get("src/main/resources/wwm.csv");

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
        List<String> split = Arrays.stream(line.split(";"))
                                   .map(s -> s.replace("\"", "").trim())
                                   .collect(Collectors.toList());
        if (split.size() != 8) {
            LOG.error("Line has not 8 parts: {}", line);
            return null;
        }

        Difficulty difficulty = mapDifficulty(Integer.parseInt(split.get(1)));
        return new Question(split.get(2), split.subList(3, 7), 0, difficulty);
    }

    private Difficulty mapDifficulty(int num) {
        if (num == 1) {
            return Difficulty.EASY;
        } else if (num == 2) {
            return Difficulty.MEDIUM;
        } else {
            return Difficulty.HARD;
        }
    }
}
