package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedbackQuestions {

    String Id = "";
    int Type = 0;
    String noofoptions = "";
    String Question = "";
    String Question_Marathi = "";
    boolean isAnswered = false;

    ArrayList<FeedbackAnswers> feedbackAnswers = new ArrayList<>();

    public FeedbackQuestions(JSONObject obj){

        try{

            if (obj.has("Id")) {
                this.Id = obj.getString("Id");
            }
            if (obj.has("Type")) {
                this.Type = obj.getInt("Type");
            }
            if (obj.has("noofoptions")) {
                this.noofoptions = obj.getString("noofoptions");
            }
            if (obj.has("Question")) {
                this.Question = obj.getString("Question");
            }
            if (obj.has("Question_Marathi")) {
                this.Question_Marathi = obj.getString("Question_Marathi");
            }

            if (obj.has("Answers")){

                Object answers = obj.get("Answers");

                if(answers instanceof JSONArray){

                    JSONArray array = obj.getJSONArray("Answers");

                    for(int i = 0; i< array.length(); i++){

                        feedbackAnswers.add(new FeedbackAnswers(array.getJSONObject(i)));

                    }

                }else{

                    feedbackAnswers.add(new FeedbackAnswers(obj.getJSONObject("Answers")));
                }

            }


        }catch (Exception e){

        }

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getNoofoptions() {
        return noofoptions;
    }

    public void setNoofoptions(String noofoptions) {
        this.noofoptions = noofoptions;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getQuestion_Marathi() {
        return Question_Marathi;
    }

    public void setQuestion_Marathi(String question_Marathi) {
        Question_Marathi = question_Marathi;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public ArrayList<FeedbackAnswers> getFeedbackAnswers() {
        return feedbackAnswers;
    }

    public void setFeedbackAnswers(ArrayList<FeedbackAnswers> feedbackAnswers) {
        this.feedbackAnswers = feedbackAnswers;
    }
}
