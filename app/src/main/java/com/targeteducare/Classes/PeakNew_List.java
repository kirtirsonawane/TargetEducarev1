package com.targeteducare.Classes;

import org.json.JSONObject;

public class PeakNew_List {

    String peakno = "";
    String peakno_InMarathi = "";
    int isExams = 0;

    public PeakNew_List(JSONObject obj){
        try{
            if(obj.has("peakno")){
                this.peakno = obj.optString("peakno");
            }

            if(obj.has("peakno_InMarathi")){
                this.peakno_InMarathi = obj.optString("peakno_InMarathi");
            }

            if(obj.has("isExams")){
                this.isExams = obj.optInt("isExams");
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getPeakno() {
        return peakno;
    }

    public void setPeakno(String peakno) {
        this.peakno = peakno;
    }

    public String getPeakno_InMarathi() {
        return peakno_InMarathi;
    }

    public void setPeakno_InMarathi(String peakno_InMarathi) {
        this.peakno_InMarathi = peakno_InMarathi;
    }

    public int getIsExams() {
        return isExams;
    }

    public void setIsExams(int isExams) {
        this.isExams = isExams;
    }
}
