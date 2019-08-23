package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class EbookChapter implements Serializable {

    String unitid = "";
    String UnitName = "";

    ArrayList<EbookContentDetails> ebookContentDetails = new ArrayList<>();


    public EbookChapter(JSONObject obj){

        if(obj.has("unitid")){
            this.unitid = obj.optString("unitid");
        }
        if(obj.has("UnitName")){
            this.UnitName = obj.optString("UnitName");
            Log.e("unitname ", obj.optString("UnitName"));
        }

        if(obj.has("ContentDetails")){

            try {
                Object contentdetailscheck = obj.opt("ContentDetails");
                if (contentdetailscheck instanceof JSONArray) {

                    JSONArray array = obj.optJSONArray("ContentDetails");

                    for (int i = 0; i < array.length(); i++) {

                        Log.e("here ebookchpter ", array.optJSONObject(i)+"");
                        ebookContentDetails.add(new EbookContentDetails(array.optJSONObject(i)));

                    }

                } else {

                    ebookContentDetails.add(new EbookContentDetails(obj.optJSONObject("ContentDetails")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public ArrayList<EbookContentDetails> getEbookContentDetails() {
        return ebookContentDetails;
    }

    public void setEbookContentDetails(ArrayList<EbookContentDetails> ebookContentDetails) {
        this.ebookContentDetails = ebookContentDetails;
    }
}
