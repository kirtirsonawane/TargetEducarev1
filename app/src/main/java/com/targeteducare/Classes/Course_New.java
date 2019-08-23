package com.targeteducare.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Course_New implements Serializable {

    String Name_InMarathi = "";
    int courseid = 0;
    String IsOmr = "";
    String ispaid = "";
    String srno = "";
    String Name = "";
    String Amount = "";

    int flag = 0;

    ArrayList<PeakNew_List> peakNew_lists = new ArrayList<>();

    public Course_New(JSONObject obj){

        try{
            if(obj.has("Name_InMarathi")){
                this.Name_InMarathi = obj.optString("Name_InMarathi");
            }
            if(obj.has("courseid")){
                this.courseid = obj.optInt("courseid");
            }
            if(obj.has("IsOmr")){
                this.IsOmr = obj.optString("IsOmr");
            }
            if(obj.has("ispaid")){
                this.ispaid = obj.optString("ispaid");
            }
            if(obj.has("srno")){
                this.srno = obj.optString("srno");
            }
            if(obj.has("Name")){
                this.Name = obj.optString("Name");
            }
            if(obj.has("Amount")){
                this.Amount = obj.optString("Amount");
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        try{

            if (obj.has("peakdetails")){

                Object answers = obj.get("peakdetails");

                if(answers instanceof JSONArray){

                    JSONArray array = obj.getJSONArray("peakdetails");

                    for(int i = 0; i< array.length(); i++){

                        peakNew_lists.add(new PeakNew_List(array.optJSONObject(i)));

                    }

                }else{

                    peakNew_lists.add(new PeakNew_List(obj.optJSONObject("peakdetails")));
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public String getName_InMarathi() {
        return Name_InMarathi;
    }

    public void setName_InMarathi(String name_InMarathi) {
        Name_InMarathi = name_InMarathi;
    }

    public int getCourseid() {
        return courseid;
    }

    public void setCourseid(int courseid) {
        this.courseid = courseid;
    }

    public String getIsOmr() {
        return IsOmr;
    }

    public void setIsOmr(String isOmr) {
        IsOmr = isOmr;
    }

    public String getIspaid() {
        return ispaid;
    }

    public void setIspaid(String ispaid) {
        this.ispaid = ispaid;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<PeakNew_List> getPeakNew_lists() {
        return peakNew_lists;
    }

    public void setPeakNew_lists(ArrayList<PeakNew_List> peakNew_lists) {
        this.peakNew_lists = peakNew_lists;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
