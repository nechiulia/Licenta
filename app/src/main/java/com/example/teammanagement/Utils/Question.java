package com.example.teammanagement.Utils;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String questionText;
    private List<String> answers;

    public Question(String questionText, List<String>answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", answers=" + answers +
                '}';
    }
}
