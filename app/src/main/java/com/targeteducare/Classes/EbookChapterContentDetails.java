package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class EbookChapterContentDetails implements Serializable {

    String EbookName = "";
    String UnitName = "";
    String ChapterName = "";
    int Id = 0;
    int EbookId = 0;
    String Type = "";
    int unitid = 0;
    int chapterid = 0;
    int Noofpages = 0;
    String MainPdf = "";
    String MarathiPdf = "";
    int progress = 0;
    int lastvisitedpage = 0;

    ArrayList<EbookPageDetails> ebookPageDetails = new ArrayList<>();
    ArrayList<EbookVideoDetails> ebookVideoDetails = new ArrayList<>();

    public EbookChapterContentDetails(JSONObject obj){

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
            this.Id = obj.optInt("Id");
        }
        if (obj.has("EbookId")) {
            this.EbookId = obj.optInt("EbookId");
        }
        if (obj.has("Type")) {
            this.Type = obj.optString("Type");
        }
        if (obj.has("unitid")) {
            this.unitid = obj.optInt("unitid");
        }
        if (obj.has("chapterid")) {
            this.chapterid = obj.optInt("chapterid");
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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getEbookId() {
        return EbookId;
    }

    public void setEbookId(int ebookId) {
        EbookId = ebookId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getUnitid() {
        return unitid;
    }

    public void setUnitid(int unitid) {
        this.unitid = unitid;
    }

    public int getChapterid() {
        return chapterid;
    }

    public void setChapterid(int chapterid) {
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getLastvisitedpage() {
        return lastvisitedpage;
    }

    public void setLastvisitedpage(int lastvisitedpage) {
        this.lastvisitedpage = lastvisitedpage;
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
