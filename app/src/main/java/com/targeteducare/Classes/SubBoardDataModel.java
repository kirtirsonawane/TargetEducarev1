package com.targeteducare.Classes;

import java.io.Serializable;

public class SubBoardDataModel implements Serializable {

    String subboard;

    public String getSubboard() {
        return subboard;
    }

    public void setSubboard(String subboard) {
        this.subboard = subboard;
    }

    public SubBoardDataModel(String subboard){
         this.subboard = subboard;
     }
}
