package com.targeteducare.Classes;

public class QnaDataModel {

    int profile_pic;
    String name;
    int minutes_ago;
    String paragraph;


    public int getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(int profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinutes_ago() {
        return minutes_ago;
    }

    public void setMinutes_ago(int minutes_ago) {
        this.minutes_ago = minutes_ago;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public QnaDataModel(int profile_pic, String name, int minutes_ago, String paragraph) {
        this.profile_pic = profile_pic;
        this.name = name;
        this.minutes_ago = minutes_ago;
        this.paragraph = paragraph;
    }
}
