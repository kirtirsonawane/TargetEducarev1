package com.targeteducare.Classes;

public class SpinnerState {
    String statename = "";
    String state_id ;

    public SpinnerState(String state_name, String id) {
        this.statename = state_name;
        this.state_id = id;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    @Override
    public String toString() {
        return this.statename;
    }
}
