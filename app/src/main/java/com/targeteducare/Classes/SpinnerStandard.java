package com.targeteducare.Classes;

public class SpinnerStandard {
    String standard = "";

    public SpinnerStandard(String subboard_name) {
        this.standard = subboard_name;
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
