package com.targeteducare.Classes;

import java.io.Serializable;

public class Exam implements Serializable {
    String categoryname = "";
    int examid = 0;
    String examname = "";
    String marks = "";
    String neagativemarks = "";
    String startdate = "";
    String enddate = "";
    String duration = "";
    String durationinMin = "";
    boolean InstantExamResult = true;
    boolean InstantExamResultWithAns = true;
    int isshowexam = 0;
    int isshowanswersheet = 0;
    String resultdate = "";
    String examresulttime = "";
    String terms = "";
    String languages = "";
    String examstarttime = "";
    String examendtime = "";
    boolean publish = true;
    int isexamindatetimerange = 0;
    boolean isdownloaded = false;
    boolean isexamgiven = false;
    boolean issync = false;
    boolean isqdownloaded = false;

    public boolean isIsqdownloaded() {
        return isqdownloaded;
    }

    public void setIsqdownloaded(boolean isqdownloaded) {
        this.isqdownloaded = isqdownloaded;
    }

    public boolean isIssync() {
        return issync;
    }

    public void setIssync(boolean issync) {
        this.issync = issync;
    }

    public boolean isIsexamgiven() {
        return isexamgiven;
    }

    public void setIsexamgiven(boolean isexamgiven) {
        this.isexamgiven = isexamgiven;
    }

    public boolean isIsdownloaded() {
        return isdownloaded;
    }

    public void setIsdownloaded(boolean isdownloaded) {
        this.isdownloaded = isdownloaded;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public int getExamid() {
        return examid;
    }

    public void setExamid(int examid) {
        this.examid = examid;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getNeagativemarks() {
        return neagativemarks;
    }

    public void setNeagativemarks(String neagativemarks) {
        this.neagativemarks = neagativemarks;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationinMin() {
        return durationinMin;
    }

    public void setDurationinMin(String durationinMin) {
        this.durationinMin = durationinMin;
    }

    public boolean isInstantExamResult() {
        return InstantExamResult;
    }

    public void setInstantExamResult(boolean instantExamResult) {
        InstantExamResult = instantExamResult;
    }

    public boolean isInstantExamResultWithAns() {
        return InstantExamResultWithAns;
    }

    public void setInstantExamResultWithAns(boolean instantExamResultWithAns) {
        InstantExamResultWithAns = instantExamResultWithAns;
    }

    public int getIsshowexam() {
        return isshowexam;
    }

    public void setIsshowexam(int isshowexam) {
        this.isshowexam = isshowexam;
    }

    public int getIsshowanswersheet() {
        return isshowanswersheet;
    }

    public void setIsshowanswersheet(int isshowanswersheet) {
        this.isshowanswersheet = isshowanswersheet;
    }

    public String getResultdate() {
        return resultdate;
    }

    public void setResultdate(String resultdate) {
        this.resultdate = resultdate;
    }

    public String getExamresulttime() {
        return examresulttime;
    }

    public void setExamresulttime(String examresulttime) {
        this.examresulttime = examresulttime;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getExamstarttime() {
        return examstarttime;
    }

    public void setExamstarttime(String examstarttime) {
        this.examstarttime = examstarttime;
    }

    public String getExamendtime() {
        return examendtime;
    }

    public void setExamendtime(String examendtime) {
        this.examendtime = examendtime;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public int getIsexamindatetimerange() {
        return isexamindatetimerange;
    }

    public void setIsexamindatetimerange(int isexamindatetimerange) {
        this.isexamindatetimerange = isexamindatetimerange;
    }
}
