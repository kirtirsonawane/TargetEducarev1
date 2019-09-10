package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class EbookPageDetails implements Serializable {
    String PageContent = "";
    int PageNo = 0;
    boolean isBookmark = false;
    boolean isFavorite = false;

    public EbookPageDetails(JSONObject obj){
        if(obj.has("PageContent")){
            this.PageContent = obj.optString("PageContent");
        }
        if(obj.has("PageNo")){
            this.PageNo = obj.optInt("PageNo");;
        }
    }

    public String getPageContent() {
        return PageContent;
    }

    public void setPageContent(String pageContent) {
        PageContent = pageContent;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int pageNo) {
        PageNo = pageNo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
