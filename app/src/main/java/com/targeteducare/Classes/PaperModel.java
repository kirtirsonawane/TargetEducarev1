package com.targeteducare.Classes;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaperModel {

    int TotalRecord;
    int Id;
    int subjectid;
    String Name;
    String Description;
    String Type;
    String VideoUrl;
    String Imagefile;
    String Subject;
    int CategoryId;
    String CategoryName;


    public PaperModel() {
    }

    public PaperModel(int totalRecord, int id, int subjectid, String name, String description, String type, String videoUrl, String imagefile, String subject, int categoryId, String categoryName) {
        this.TotalRecord = totalRecord;
        this.Id = id;
        this.subjectid = subjectid;
        this.Name = name;
        this.Description = description;
        this.Type = type;
        this.VideoUrl = videoUrl;
        this.Imagefile = imagefile;
        this.Subject = subject;
        this.CategoryId = categoryId;
        this.CategoryName = categoryName;
    }

    public static ArrayList<PaperModel> getjson(JSONObject jsonObject3) {
        ArrayList<PaperModel> modelArrayList = null;
        PaperModel paperModel = null;
        try {


            JSONArray jsonArray1 = null;
            jsonArray1 = jsonObject3.optJSONArray("subroot");


            if (jsonArray1 != null) {
                Gson gson = new Gson();
                //JSONArray jsonArray = jsonObject3.getJSONArray("subroot");
                java.lang.reflect.Type type = new TypeToken<ArrayList<PaperModel>>() {
                }.getType();
                modelArrayList = gson.fromJson(String.valueOf(jsonArray1), type);
            }





            else{

               JSONObject jsonObject4 = jsonObject3.optJSONObject("subroot");
                if (jsonObject4 != null) {
                    int i = 0;

                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<ArrayList<PaperModel>>() {
                    }.getType();

                    paperModel = gson.fromJson(String.valueOf(jsonObject4), type);
                    Log.e("Array list:: ", modelArrayList.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return modelArrayList;
    }


    public int getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        TotalRecord = totalRecord;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public String getImagefile() {
        return Imagefile;
    }

    public void setImagefile(String imagefile) {
        Imagefile = imagefile;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }


    @Override
    public String toString() {
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(Name);
        arrayList.add(CategoryName);
        return arrayList.toString();

    }
}