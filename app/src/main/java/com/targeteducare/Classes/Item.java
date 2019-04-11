package com.targeteducare.Classes;

import java.util.ArrayList;

/**
 * Created by bpncool on 2/23/2016.
 */
public class Item {
    private String name = "";
    private int id = 0;
    private String noofques = "";
    private int subjectid = 0;
    ArrayList<Question> qdata = new ArrayList<>();
    int totalwrong = 0;
    int totalquestions = qdata.size();
    int totalright = 0;
    double obtainedmarks=0;
    double negativemarks=0;
    int totalnotanswered = 0;

    public int getTotalwrong() {
        return totalwrong;
    }

    public void setTotalwrong(int totalwrong) {
        this.totalwrong = totalwrong;
    }

    public int getTotalquestions() {
        return totalquestions;
    }

    public void setTotalquestions(int totalquestions) {
        this.totalquestions = totalquestions;
    }

    public int getTotalright() {
        return totalright;
    }

    public void setTotalright(int totalright) {
        this.totalright = totalright;
    }

    public double getObtainedmarks() {
        return obtainedmarks;
    }

    public void setObtainedmarks(double obtainedmarks) {
        this.obtainedmarks = obtainedmarks;
    }

    public double getNegativemarks() {
        return negativemarks;
    }

    public void setNegativemarks(double negativemarks) {
        this.negativemarks = negativemarks;
    }

    public int getTotalnotanswered() {
        return totalnotanswered;
    }

    public void setTotalnotanswered(int totalnotanswered) {
        this.totalnotanswered = totalnotanswered;
    }

    public Item(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Item() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNoofques() {
        return noofques;
    }

    public void setNoofques(String noofques) {
        this.noofques = noofques;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public ArrayList<Question> getQdata() {
        return qdata;
    }

    public void setQdata(ArrayList<Question> qdata) {
        this.qdata = qdata;
    }
}
