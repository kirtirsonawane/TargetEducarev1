package com.targeteducare.Classes;

public class PaperModel {
    int papername;
    String downloadorread;

    public int getPapername() {
        return papername;
    }

    public void setPapername(int papername) {
        this.papername = papername;
    }

    public String getDownloadorread() {
        return downloadorread;
    }

    public void setDownloadorread(String downloadorread) {
        this.downloadorread = downloadorread;
    }

    public PaperModel(int papername, String downloadorread){
        this.downloadorread = downloadorread;
        this.papername = papername;
    }
}
