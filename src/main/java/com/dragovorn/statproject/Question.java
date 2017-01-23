package com.dragovorn.statproject;

public class Question {

    private String questionText;

    private String[] answers;

    private int correct;

    public Question(String questionText, String answer1, String answer2, String answer3, String answer4, int correct) {
        this.questionText = questionText;
        this.answers = new String[] { answer1, answer2, answer3, answer4 };
        this.correct = correct;
    }

    public String getQuestionText() {
        return this.questionText;
    }

    public String[] getAnswers() {
        return this.answers;
    }

    public int getCorrect() {
        return this.correct;
    }
}