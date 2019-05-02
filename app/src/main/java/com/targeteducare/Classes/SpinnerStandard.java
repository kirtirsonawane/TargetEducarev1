package com.targeteducare.Classes;

public class SpinnerStandard {
    String standard = "";
    String id = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpinnerStandard(String subboard_name, String id_subboard) {
        this.standard = subboard_name;
        this.id = id_subboard;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    @Override
    public String toString() {
        return this.standard;
    }
}
