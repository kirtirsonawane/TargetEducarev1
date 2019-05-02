package com.targeteducare.Classes;

public class SpinnerCity {

    String city_name = " ";
    String city_id = "";

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public SpinnerCity(String city_name, String id){
        this.city_name = city_name;
        this.city_id = id;
    }

    @Override
    public String toString() {
        return this.city_name;
    }
}
