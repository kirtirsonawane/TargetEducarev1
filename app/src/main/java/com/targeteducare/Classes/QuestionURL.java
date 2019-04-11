package com.targeteducare.Classes;

public class QuestionURL {
    int id1=0;
    int id=0;
    String imagemainsource="";
    String type="";

   public QuestionURL(){

   }

   public QuestionURL(String url){
    this.imagemainsource=url;
   }
    public QuestionURL(String url, String type, int id){
        this.imagemainsource=url;
        this.type=type;
        this.id=id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagemainsource() {
        return imagemainsource;
    }

    public void setImagemainsource(String imagemainsource) {
        this.imagemainsource = imagemainsource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
