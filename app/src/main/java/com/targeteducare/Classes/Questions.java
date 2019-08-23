package com.targeteducare.Classes;

import org.json.JSONObject;

import java.io.Serializable;

public class Questions implements Serializable  {

  /*  "Id": "5",
            "CategoryId": "12",
            "CategoryName": "X",
            "Title": "How do airplanes fly upside down if it's the shape of the wings that make them fly?",
            "TitleInMarathi": "पदव्युत्तर वैद्यकीय प्रवेशासंबंधी नागपूर खंडपीठाच्या निर्णयामुळे वैद्यकीय शिक्षण विभागासमोर पेच निर्माण झाला आहे.",
            "Explanation": "<p>How do airplanes fly upside down if it's the shape of the wings that make them fly?<br></p>",
            "Sort_Order":*/
        String Id;
        String CategoryId;
        String CategoryName;
        String Title;
        String TitleInMarathi;
        String Explanation;
        String Sort_Order;
        String ExplanationInMarathi;

    public Questions(String id, String categoryId, String categoryName, String title, String titleInMarathi, String explanation, String sort_Order,String ExplanationInMarathi) {
        this.Id = id;
        this.CategoryId = categoryId;
        this.CategoryName = categoryName;
        this.Title = title;
        this.TitleInMarathi = titleInMarathi;
        this.Explanation = explanation;
        this.Sort_Order = sort_Order;
        this.ExplanationInMarathi=ExplanationInMarathi;
    }


    public Questions() {
    }

    public static Questions getjsonforquestions(JSONObject object1) {
        Questions questions=new Questions();
        try {
            if (object1.has("Id")){
                questions.setId(object1.getString("Id"));
            }
            else {
                questions.setId((""));
            }
            if (object1.has("CategoryName")){
                questions.setCategoryName(object1.getString("CategoryName"));
            }
            else {
                questions.setCategoryName((""));
            }

            if (object1.has("CategoryId")){
                questions.setCategoryId(object1.getString("CategoryId"));
            }
            else {
                questions.setCategoryId((""));
            }
            if (object1.has("Explanation")){
                questions.setExplanation(object1.getString("Explanation"));
            }
            else {
                questions.setExplanation((""));
            }
            if (object1.has("TitleInMarathi")){
                questions.setTitleInMarathi(object1.getString("TitleInMarathi"));
            }
            else {
                questions.setTitleInMarathi((""));
            }
            if (object1.has("Title")){
                questions.setTitle(object1.getString("Title"));
            }
            else {
                questions.setTitle((""));
            }
            if (object1.has("Sort_Order")){
                questions.setSort_Order(object1.getString("Sort_Order"));
            }
            else {
                questions.setSort_Order((""));
            }
            if (object1.has("ExplanationInMarathi")) {
                questions.setExplanationInMarathi(object1.getString("ExplanationInMarathi"));
            }
            else {
                questions.setExplanationInMarathi((""));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return questions;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.CategoryName = categoryName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getTitleInMarathi() {
        return TitleInMarathi;
    }

    public void setTitleInMarathi(String titleInMarathi) {
        this.TitleInMarathi = titleInMarathi;
    }

    public String getExplanation() {
        return Explanation;
    }

    public void setExplanation(String explanation) {
        this.Explanation = explanation;
    }

    public String getSort_Order() {
        return Sort_Order;
    }

    public void setSort_Order(String sort_Order) {
        this.Sort_Order = sort_Order;
    }

    public String getExplanationInMarathi() {
        return ExplanationInMarathi;
    }

    public void setExplanationInMarathi(String explanationInMarathi) {
        this.ExplanationInMarathi = explanationInMarathi;
    }
}
