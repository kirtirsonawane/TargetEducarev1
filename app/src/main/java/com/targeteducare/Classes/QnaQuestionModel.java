package com.targeteducare.Classes;

import java.util.ArrayList;

public class QnaQuestionModel {
    String main_question;
    int followers;
    int answers;

    ArrayList<QnaDataModel> qndataset = new ArrayList<>();

    public ArrayList<QnaDataModel> getQndataset() {
        return qndataset;
    }

    public void setQndataset(ArrayList<QnaDataModel> qndataset) {
        this.qndataset = qndataset;
    }

    public String getMain_question() {
        return main_question;
    }

    public void setMain_question(String main_question) {
        this.main_question = main_question;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }

    public QnaQuestionModel(String main_question, int followers, int answers) {
        this.main_question = main_question;
        this.followers = followers;
        this.answers = answers;
    }
}
