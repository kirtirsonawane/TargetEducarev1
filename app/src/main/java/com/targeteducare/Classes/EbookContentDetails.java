package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class EbookContentDetails implements Serializable {

    String EbookName = "";
    String UnitName = "";
    String ChapterName = "";
    String Id = "";
    String Type = "";
    String unitid = "";
    String chapterid = "";
    int Noofpages = 0;
    String MainPdf = "";
    String MarathiPdf = "";
    ArrayList<EbookPageDetails> ebookPageDetails = new ArrayList<>();
    ArrayList<EbookVideoDetails> ebookVideoDetails = new ArrayList<>();

    public EbookContentDetails(JSONObject obj){

        if (obj.has("EbookName")) {
            this.EbookName = obj.optString("EbookName");
        }
        if (obj.has("UnitName")) {
            this.UnitName = obj.optString("UnitName");
        }
        if (obj.has("ChapterName")) {
            this.ChapterName = obj.optString("ChapterName");
        }
        if (obj.has("Id")) {
            this.Id = obj.optString("Id");
        }
        if (obj.has("Type")) {
            this.Type = obj.optString("Type");
        }
        if (obj.has("unitid")) {
            this.unitid = obj.optString("unitid");
        }
        if (obj.has("chapterid")) {
            this.chapterid = obj.optString("chapterid");
        }
        if (obj.has("Noofpages")) {
            this.Noofpages = obj.optInt("Noofpages");
        }
        if (obj.has("MainPdf")) {
            this.MainPdf = obj.optString("MainPdf");
        }
        if (obj.has("MarathiPdf")) {
            this.MarathiPdf = obj.optString("MarathiPdf");
        }
        if(obj.has("PageDetails")){
            try {
                Object answers = obj.get("PageDetails");

                if (answers instanceof JSONArray) {

                    JSONArray array = obj.getJSONArray("PageDetails");

                    for (int i = 0; i < array.length(); i++) {

                        ebookPageDetails.add(new EbookPageDetails(array.optJSONObject(i)));

                    }

                } else {

                    ebookPageDetails.add(new EbookPageDetails(obj.optJSONObject("PageDetails")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(obj.has("VideoDetails")){
            try {
                Object answers = obj.get("VideoDetails");

                if (answers instanceof JSONArray) {

                    JSONArray array = obj.getJSONArray("VideoDetails");

                    for (int i = 0; i < array.length(); i++) {

                        Log.e("ebookvideo array ", array.optJSONObject(i)+"");

                        ebookVideoDetails.add(new EbookVideoDetails(array.optJSONObject(i)));

                    }

                } else {

                    ebookVideoDetails.add(new EbookVideoDetails(obj.optJSONObject("VideoDetails")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public String getEbookName() {
        return EbookName;
    }

    public void setEbookName(String ebookName) {
        EbookName = ebookName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getChapterName() {
        return ChapterName;
    }

    public void setChapterName(String chapterName) {
        ChapterName = chapterName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getChapterid() {
        return chapterid;
    }

    public void setChapterid(String chapterid) {
        this.chapterid = chapterid;
    }

    public int getNoofpages() {
        return Noofpages;
    }

    public void setNoofpages(int noofpages) {
        Noofpages = noofpages;
    }

    public String getMainPdf() {
        return MainPdf;
    }

    public void setMainPdf(String mainPdf) {
        MainPdf = mainPdf;
    }

    public String getMarathiPdf() {
        return MarathiPdf;
    }

    public void setMarathiPdf(String marathiPdf) {
        MarathiPdf = marathiPdf;
    }

    public ArrayList<EbookPageDetails> getEbookPageDetails() {
        return ebookPageDetails;
    }

    public void setEbookPageDetails(ArrayList<EbookPageDetails> ebookPageDetails) {
        this.ebookPageDetails = ebookPageDetails;
    }

    public ArrayList<EbookVideoDetails> getEbookVideoDetails() {
        return ebookVideoDetails;
    }

    public void setEbookVideoDetails(ArrayList<EbookVideoDetails> ebookVideoDetails) {
        this.ebookVideoDetails = ebookVideoDetails;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
