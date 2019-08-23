package com.targeteducare.Classes;

import android.widget.TextView;

import org.json.JSONObject;

import java.io.Serializable;

public class EventModel implements Serializable {

    String TotalRecord = "";
    String Id = "";
    String EventDate = "";
    String Title = "";
    String Venue = "";
    String Moderator = "";
    String Publish = "";
    String CreatedDate = "";
    String Title_Marathi = "";

    public String isSubmitFeedback = "0";


    public EventModel(JSONObject obj){

        try {
            if (obj.has("TotalRecord")) {
                this.TotalRecord = obj.getString("TotalRecord");
            }
            if (obj.has("Id")) {
                this.Id = obj.getString("Id");
            }
            if (obj.has("EventDate")) {
                this.EventDate = obj.getString("EventDate");
            }
            if (obj.has("Title")) {
                this.Title = obj.getString("Title");
            }
            if (obj.has("Title_Marathi")) {
                this.Title_Marathi = obj.getString("Title_Marathi");
            }
            if (obj.has("Venue")) {
                this.Venue = obj.getString("Venue");
            }
            if (obj.has("Moderator")) {
                this.Moderator = obj.getString("Moderator");
            }
            if (obj.has("Publish")) {
                this.Publish = obj.getString("Publish");
            }
            if (obj.has("CreatedDate")) {
                this.CreatedDate = obj.getString("CreatedDate");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getTotalRecord() {
        return TotalRecord;
    }

    public void setTotalRecord(String totalRecord) {
        TotalRecord = totalRecord;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle_Marathi() {
        return Title_Marathi;
    }

    public void setTitle_Marathi(String title_Marathi) {
        Title_Marathi = title_Marathi;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getModerator() {
        return Moderator;
    }

    public void setModerator(String moderator) {
        Moderator = moderator;
    }

    public String getPublish() {
        return Publish;
    }

    public void setPublish(String publish) {
        Publish = publish;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getIsSubmitFeedback() {
        return isSubmitFeedback;
    }

    public void setIsSubmitFeedback(String isSubmitFeedback) {
        this.isSubmitFeedback = isSubmitFeedback;
    }

    /*String event_name = "";
    public EventModel(String event_name){
        this.event_name = event_name;
    }*/

}
