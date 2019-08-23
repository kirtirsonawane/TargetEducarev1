package com.targeteducare.Classes;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

public class EbookPageDetails implements Serializable {
    String PageContent = "";
    int PageId = 0;
    boolean isBookmark = false;
    boolean isFavorite = false;

    public EbookPageDetails(JSONObject obj){
        if(obj.has("PageContent")){
            this.PageContent = obj.optString("PageContent");
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

    public int getPageId() {
        return PageId;
    }

    public void setPageId(int pageId) {
        PageId = pageId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
