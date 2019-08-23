package com.targeteducare.Classes;

import org.json.JSONObject;

import java.io.Serializable;

public class EbookDetails implements Serializable {

    int Id = 0;
    String subjectid = "";
    String Name = "";
    String Description = "";
    String Type = "";
    String Subject = "";
    String VideoUrl = "";
    String Imagefile = "";

    public EbookDetails(JSONObject obj){

        if(obj.has("Id")){
            this.Id = obj.optInt("Id");
        }
        if(obj.has("subjectid")){
            this.subjectid = obj.optString("subjectid");
        }
        if(obj.has("Name")){
            this.Name = obj.optString("Name");
        }

        if(obj.has("Description")){
            this.Description = obj.optString("Description");
        }
        if(obj.has("Type")){
            this.Type = obj.optString("Type");
        }
        if(obj.has("Subject")){
            this.Subject = obj.optString("Subject");
        }

        if(obj.has("VideoUrl")){
            this.VideoUrl = obj.optString("VideoUrl");
        }
        if(obj.has("Subject")){
            this.Imagefile = obj.optString("Imagefile");
        }

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
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

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
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

    @Override
    public String toString() {
        return super.toString();
    }
}
