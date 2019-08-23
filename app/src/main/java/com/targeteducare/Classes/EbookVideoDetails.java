package com.targeteducare.Classes;

import org.json.JSONObject;

import java.io.Serializable;

public class EbookVideoDetails implements Serializable {

    String videotitle = "";
    String video = "";
    String videoimg = "";

    public EbookVideoDetails(JSONObject obj){

        if(obj.has("videotitle")){
            this.videotitle = obj.optString("videotitle");
        }

        if(obj.has("video")){
            this.video = obj.optString("video");
        }

        if(obj.has("videoimg")){
            this.videoimg = obj.optString("videoimg");
        }

    }

    public String getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(String videotitle) {
        this.videotitle = videotitle;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoimg() {
        return videoimg;
    }

    public void setVideoimg(String videoimg) {
        this.videoimg = videoimg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
