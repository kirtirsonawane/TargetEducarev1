package com.targeteducare.Classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONObject;

public class FeedbackAnswers {

    String Id = "";
    String Answer = "";
    String Answer_Marathi = "";
    String Ans = "";

    boolean isSelected = false;


    public FeedbackAnswers(JSONObject obj){

        try {

            if (obj.has("Id")) {
                this.Id = obj.getString("Id");
            }
            if (obj.has("Answer")) {
                this.Answer = obj.getString("Answer");
            }
            if (obj.has("Answer_Marathi")) {
                this.Answer_Marathi = obj.getString("Answer_Marathi");
            }
            if (obj.has("Ans")) {
                this.Ans = obj.getString("Ans");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getAnswer_Marathi() {
        return Answer_Marathi;
    }

    public void setAnswer_Marathi(String answer_Marathi) {
        Answer_Marathi = answer_Marathi;
    }

    public String getAns() {
        return Ans;
    }

    public void setAns(String ans) {
        Ans = ans;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
