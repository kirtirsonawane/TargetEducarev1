package com.targeteducare.Classes;


import java.util.ArrayList;

public class SplashModel {

    String Id;
    String Type;
    String Title;
    String Description;
    String FromDate;
    String ToDate;
    String CreatedDate;
    String Title_Marathi;
    String Description_Marathi;
                    /*"Id": "2",
                    "Type": "Announcement",
                    "Title": "Test2",
                    "Description": "<p><span style=\"color: rgb(34, 34, 34); font-family: arial, sans-serif; font-size: small;\">Description is the pattern of narrative development that aims to make vivid a place, object, character, or group. Description is one of four rhetorical modes, along with exposition, argumentation, and narration. In practice it would be difficult to write literature that drew on just one of the four basic modes</span><br></p>",
                    "FromDate": "06-06-2019",
                    "ToDate": "06-25-2019",
                    "CreatedDate": "201*/

    public SplashModel(String id, String type, String title, String description, String fromDate, String toDate, String createdDate, String Description_Marathi,String Title_Marathi) {
        Id = id;
        Type = type;
        Title = title;
        Description = description;
        FromDate = fromDate;
        ToDate = toDate;
        CreatedDate = createdDate;
        Title_Marathi=Title_Marathi;
        Description_Marathi=Description_Marathi;
    }


    public SplashModel() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        this.FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        this.ToDate = toDate;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    public String getTitle_Marathi() {
        return Title_Marathi;
    }

    public void setTitle_Marathi(String title_Marathi) {
        Title_Marathi = title_Marathi;
    }

    public String getDescription_Marathi() {
        return Description_Marathi;
    }

    public void setDescription_Marathi(String description_Marathi) {
        Description_Marathi = description_Marathi;
    }

    @Override
    public String toString() {


        ArrayList list = new ArrayList();

        //list.add(Id) ;
        list.add(Title);
        //list.add(question);
        //list.add (answers);
        list.add(Description);
        list.add(FromDate);
        list.add(Description_Marathi);
        list.add(Title_Marathi);
        //list.add (SenderId);
        //list.add (StudentId);
        //list.add();



        return String.valueOf(list);
    }
}


