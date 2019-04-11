package com.targeteducare.Classes;

public class SubjectPurchaseModel {

    int icon_topictopurchase;
    String topic_nameforsubject = "";
    int price_of_topic;

    public SubjectPurchaseModel(int icon_topictopurchase, String topic_nameforsubject, int price_of_topic){
        this.icon_topictopurchase = icon_topictopurchase;
        this.topic_nameforsubject = topic_nameforsubject;
        this.price_of_topic = price_of_topic;
    }
}
