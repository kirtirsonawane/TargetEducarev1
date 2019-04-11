package com.targeteducare.Classes;

public class SpinnerCity {

    String city_name = " ";
    String state_id;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public SpinnerCity(String city_name, String id){
        this.city_name = city_name;
        this.state_id = id;
    }

    @Override
    public String toString() {
        return this.city_name;
    }
}
