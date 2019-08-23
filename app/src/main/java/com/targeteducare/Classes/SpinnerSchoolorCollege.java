package com.targeteducare.Classes;

public class SpinnerSchoolorCollege {

    String sanstha_id = "";
    String name_school_college = "";

    public String getSanstha_id() {
        return sanstha_id;
    }

    public void setSanstha_id(String sanstha_id) {
        this.sanstha_id = sanstha_id;
    }

    public String getName_school_college() {
        return name_school_college;
    }

    public void setName_school_college(String name_school_college) {
        this.name_school_college = name_school_college;
    }

    public  SpinnerSchoolorCollege(String sanstha_id, String name_school_college){
        this.sanstha_id = sanstha_id;
        this.name_school_college = name_school_college;
    }

    @Override
    public String toString() {
        return this.name_school_college;
    }
}
