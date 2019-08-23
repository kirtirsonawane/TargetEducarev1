package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class EbookSubjects implements Serializable {

    String subjectid = "";
    String subjctname = "";
    boolean isSelected = false;
    ArrayList<EbookDetails> ebookDetails = new ArrayList<>();

    public EbookSubjects(JSONObject obj){

        if(obj.has("subjectid")){
            this.subjectid = obj.optString("subjectid");
        }
        if(obj.has("subjctname")){
            this.subjctname = obj.optString("subjctname");
        }

        if(obj.has("EbookDetails")){

            try {
                Object answers = obj.get("EbookDetails");

                if (answers instanceof JSONArray) {

                    JSONArray array = obj.getJSONArray("EbookDetails");

                    for (int i = 0; i < array.length(); i++) {

                        ebookDetails.add(new EbookDetails(array.optJSONObject(i)));

                    }

                } else {

                    ebookDetails.add(new EbookDetails(obj.optJSONObject("EbookDetails")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjctname() {
        return subjctname;
    }

    public void setSubjctname(String subjctname) {
        this.subjctname = subjctname;
    }

    public ArrayList<EbookDetails> getEbookDetails() {
        return ebookDetails;
    }

    public void setEbookDetails(ArrayList<EbookDetails> ebookDetails) {
        this.ebookDetails = ebookDetails;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
