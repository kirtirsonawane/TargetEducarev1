package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SubCategoryModel implements Serializable {
    String Id;
    String Name;
    String NameInMarathi;
    String ParentId;
    ArrayList<Questions> questions1;



   // SubCategoryModel subCategoryModel=new SubCategoryModel();
    /*"Id": "12",
            "Name": "X",
            "NameInMarathi": "क्षक्ष",
            "ParentId": "3",*/


    public SubCategoryModel(String Id, String Name, String NameInMarathi, String ParentId, ArrayList<Questions> questions1) {
       this.Id = Id;
        this.Name = Name;
        this.NameInMarathi = NameInMarathi;
        this.ParentId = ParentId;
        this.questions1 = questions1;
    }

    public SubCategoryModel() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNameInMarathi() {
        return NameInMarathi;
    }

    public void setNameInMarathi(String nameInMarathi) {
        this.NameInMarathi = nameInMarathi;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        this.ParentId = parentId;
    }

    public ArrayList<Questions> getQuestions1() {
        return questions1;
    }

    public void setQuestions1(ArrayList<Questions> questions1) {
        this.questions1 = questions1;
    }

    public static SubCategoryModel getjsonforsubcategory(JSONObject object1) {
        SubCategoryModel subCategoryModel=new SubCategoryModel();
        try {

            JSONObject jsonObject=object1.optJSONObject("SubCategory");




            subCategoryModel.setId(object1.getString("Id"));

            subCategoryModel.setName(object1.getString("Name"));
            subCategoryModel.setNameInMarathi(object1.getString("NameInMarathi"));
            subCategoryModel.setParentId(object1.getString("ParentId"));
            ArrayList<Questions> questions = new ArrayList<>();
            if (object1.opt("Questions") != null) {

                questions.add(Questions.getjsonforquestions(object1.getJSONObject("Questions")));


            }
            else if (object1.opt("Questions") == null) {

            JSONArray jsonArray = object1.optJSONArray("Questions");
             if(jsonArray!=null){
                 for (int j = 0; j < jsonArray.length(); j++) {
                     JSONObject object = jsonArray.getJSONObject(j);
                subCategoryModel.setQuestions1(questions);
                questions.add(Questions.getjsonforquestions(object));}
            }
             else{

                 subCategoryModel.setQuestions1(questions);



             }


            }
            subCategoryModel.setQuestions1(questions);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subCategoryModel;
    }
}
