package com.example.davidalaw.bsllearningtool.mData;

/**
 * Setters and getters for QuestionBank object
 *
 * Created by DavidALaw on 26/07/2017.
 */

public class QuestionBank {

    private int id;
    private String choiceA, choiceB, choiceC, choiceD, correctAnswer;
    private int sign_fk;

    //Default constructor
    public QuestionBank() {
    }

    public QuestionBank(String choiceA, String answerB, String choiceC, String choiceD, String correctAnswer, int sign_fk) {
        this.choiceA = choiceA;
        this.choiceB = answerB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctAnswer = correctAnswer;
        this.sign_fk = sign_fk;
    }

    public QuestionBank(String line) {
        String[] tokens = line.split(";");
        choiceA = tokens[0];
        choiceB = tokens[1];
        choiceC = tokens[2];
        choiceD = tokens[3];
        correctAnswer = tokens[4];
        sign_fk = Integer.parseInt(tokens[5]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSign_fk() {
        return sign_fk;
    }

    public void setSign_fk(int sign_fk) {
        this.sign_fk = sign_fk;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getChoiceD() {
        return choiceD;
    }

    public void setChoiceD(String choiceD) {
        this.choiceD = choiceD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
