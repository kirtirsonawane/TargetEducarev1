package com.targeteducare.Classes;

public class SpinnerCenter {
    String sanstha_id = "";
    String name_sanstha = "";
    String center_id = "";

    public SpinnerCenter(String center_id, String sanstha_id, String name_sanstha){
        this.sanstha_id = sanstha_id;
        this.name_sanstha = name_sanstha;
        this.center_id = center_id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getSanstha_id() {
        return sanstha_id;
    }

    public void setSanstha_id(String sanstha_id) {
        this.sanstha_id = sanstha_id;
    }

    public String getName_sanstha() {
        return name_sanstha;
    }

    public void setName_sanstha(String name_sanstha) {
        this.name_sanstha = name_sanstha;
    }

    @Override
    public String toString() {
        return this.name_sanstha;
    }
}
