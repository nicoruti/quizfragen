package ch.srf.inte.quizfragen.model;

import lombok.Value;

import java.util.List;

@Value
public class Question {
    String question;
    List<String> answers;
    int correctAnswerIndex;
    Difficulty difficulty;
}
