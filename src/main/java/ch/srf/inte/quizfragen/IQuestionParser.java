package ch.srf.inte.quizfragen;

import ch.srf.inte.quizfragen.model.Question;

import java.io.IOException;
import java.util.List;

public interface IQuestionParser {
    List<Question> getQuestions() throws IOException;
}
