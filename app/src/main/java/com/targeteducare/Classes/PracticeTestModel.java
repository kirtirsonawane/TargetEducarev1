package com.targeteducare.Classes;

public class PracticeTestModel {

    String topic_no = "";
    String topic_name_select = "";
    String total_videos = "";
    String total_goals = "";
    String total_concepts = "";
    String covered_percentage = "";

    public PracticeTestModel(String topic_no, String topic_name_select, String total_videos, String total_goals,
                             String total_concepts, String covered_percentage){
        this.topic_no = topic_no;
        this.topic_name_select = topic_name_select;
        this.total_videos = total_videos;
        this.total_goals = total_goals;
        this.total_concepts = total_concepts;
        this.covered_percentage = covered_percentage;
    }

    public String getTopic_no() {
        return topic_no;
    }

    public void setTopic_no(String topic_no) {
        this.topic_no = topic_no;
    }

    public String getTopic_name_select() {
        return topic_name_select;
    }

    public void setTopic_name_select(String topic_name_select) {
        this.topic_name_select = topic_name_select;
    }

    public String getTotal_videos() {
        return total_videos;
    }

    public void setTotal_videos(String total_videos) {
        this.total_videos = total_videos;
    }

    public String getTotal_goals() {
        return total_goals;
    }

    public void setTotal_goals(String total_goals) {
        this.total_goals = total_goals;
    }

    public String getTotal_concepts() {
        return total_concepts;
    }

    public void setTotal_concepts(String total_concepts) {
        this.total_concepts = total_concepts;
    }

    public String getCovered_percentage() {
        return covered_percentage;
    }

    public void setCovered_percentage(String covered_percentage) {
        this.covered_percentage = covered_percentage;
    }
}
