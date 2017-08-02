package com.example.davidalaw.bsllearningtool.mSQLiteHandler;

/**
 * Created by DavidALaw on 26/07/2017.
 */

public class QuestionBank {

    private int id;
    private String category, videoURI, answerA, answerB, answerC, answerD, correctAnswer;

    public QuestionBank() {
    }

    public QuestionBank(int id, String category, String videoURI, String answerA, String answerB,
                        String answerC, String answerD, String correctAnswer) {
        this.id = id;
        this.category = category;
        this.videoURI = videoURI;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public QuestionBank(String category, String videoURI, String answerA, String answerB,
                        String answerC, String answerD, String correctAnswer) {
        this.category = category;
        this.videoURI = videoURI;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.correctAnswer = correctAnswer;
    }

    public QuestionBank(String line) {
        String [] tokens = line.split(";");

        category = tokens[0];
        videoURI = tokens[1];
        answerA = tokens[2];
        answerB = tokens[3];
        answerC = tokens[4];
        answerD = tokens[5];
        correctAnswer = tokens[6];

    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(String videoURI) {
        this.videoURI = videoURI;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
