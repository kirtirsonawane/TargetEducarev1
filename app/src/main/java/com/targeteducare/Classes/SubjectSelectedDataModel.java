package com.targeteducare.Classes;

public class SubjectSelectedDataModel {

    int icon_topic;
    String name_student = "";
    String random_text = "";
    String topic_name = "";

    public SubjectSelectedDataModel(int icon_topic, String name_student, String random_text , String topic_name){
        this.icon_topic = icon_topic;
        this.name_student = name_student;
        this.random_text = random_text;
        this.topic_name = topic_name;
    }

    public int getIcon_topic() {
        return icon_topic;
    }

    public void setIcon_topic(int icon_topic) {
        this.icon_topic = icon_topic;
    }

    public String getName_student() {
        return name_student;
    }

    public void setName_student(String name_student) {
        this.name_student = name_student;
    }

    public String getRandom_text() {
        return random_text;
    }

    public void setRandom_text(String random_text) {
        this.random_text = random_text;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }
}
