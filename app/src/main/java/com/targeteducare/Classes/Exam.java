package com.targeteducare.Classes;

import java.io.Serializable;

public class Exam implements Serializable {
    String categoryname = "";
    int examid = 0;
    String examname = "";
    String marks = "0";
    String neagativemarks = "0";
    String startdate = "";
    String enddate = "";
    String duration = "0";
    String durationinMin = "0";
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
    Double progress = 0.0;
    int answered = 0;
    int wrong = 0;
    int skipp = 0;
    int correct = 0;
    long timetaken = 0;
    String exam_type = "";
    Double speed = 0.0;
    String coursename = "";
    String courseid = "";
    String coursename_inmarathi = "";
    String Name_InMarathi = "";
    boolean isheader = false;
    int ispaid = 0;
    String examstatus = "";
    String version = "1";
    String DayMonthNumber;
    int IsOmr = 0;
    String type="";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsOmr() {
        return IsOmr;
    }

    public void setIsOmr(int isOmr) {
        IsOmr = isOmr;
    }

    public String getDayMonthNumber() {
        return DayMonthNumber;
    }

    public void setDayMonthNumber(String dayMonthNumber) {
        DayMonthNumber = dayMonthNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExamstatus() {
        return examstatus;
    }

    public void setExamstatus(String examstatus) {
        this.examstatus = examstatus;
    }

    public int getIspaid() {
        return ispaid;
    }

    public void setIspaid(int ispaid) {
        this.ispaid = ispaid;
    }

    public boolean isIsheader() {
        return isheader;
    }

    public void setIsheader(boolean isheader) {
        this.isheader = isheader;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCoursename_inmarathi() {
        return coursename_inmarathi;
    }

    public void setCoursename_inmarathi(String coursename_inmarathi) {
        this.coursename_inmarathi = coursename_inmarathi;
    }

    public String getName_InMarathi() {
        return Name_InMarathi;
    }

    public void setName_InMarathi(String name_InMarathi) {
        Name_InMarathi = name_InMarathi;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getSkipp() {
        return skipp;
    }

    public void setSkipp(int skipp) {
        this.skipp = skipp;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public long getTimetaken() {
        return timetaken;
    }

    public void setTimetaken(long timetaken) {
        this.timetaken = timetaken;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

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


    //For Different Test ACtivities
    String topic_no = "";
    String topic_name_select = "";
    String total_questions = "";
    String total_videos = "";
    String total_concepts = "";
    String covered_percentage = "";

    /*public Exam(String topic_no, String topic_name_select, String total_videos, String total_goals,
                             String total_concepts, String covered_percentage){
        this.topic_no = topic_no;
        this.topic_name_select = topic_name_select;
        this.total_videos = total_videos;
        this.total_goals = total_goals;
        this.total_concepts = total_concepts;
        this.covered_percentage = covered_percentage;
    }*/

    public String getTopic_no() {
        return topic_no;
    }

    public void setTopic_no(String topic_no) {
        this.topic_no = topic_no;
    }

    public String getTopic_name_select() {
        return topic_name_select;
    }

    public void setTopic_name_select(String topic_name_select) {
        this.topic_name_select = topic_name_select;
    }

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(String total_questions) {
        this.total_questions = total_questions;
    }

    public String getTotal_videos() {
        return total_videos;
    }

    public void setTotal_videos(String total_videos) {
        this.total_videos = total_videos;
    }

    public String getTotal_concepts() {
        return total_concepts;
    }

    public void setTotal_concepts(String total_concepts) {
        this.total_concepts = total_concepts;
    }

    public String getCovered_percentage() {
        return covered_percentage;
    }

    public void setCovered_percentage(String covered_percentage) {
        this.covered_percentage = covered_percentage;
    }


    //For Progress Report

    String current_progress = "";
    String questions_exam = "";
    String total_correct = "";
    String total_skipped = "";
    String total_wrong = "";
    String total_speed = "";
    String total_timetaken = "";

    public String getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(String current_progress) {
        this.current_progress = current_progress;
    }

    public String getQuestions_exam() {
        return questions_exam;
    }

    public void setQuestions_exam(String questions_exam) {
        this.questions_exam = questions_exam;
    }

    public String getTotal_correct() {
        return total_correct;
    }

    public void setTotal_correct(String total_correct) {
        this.total_correct = total_correct;
    }

    public String getTotal_skipped() {
        return total_skipped;
    }

    public void setTotal_skipped(String total_skipped) {
        this.total_skipped = total_skipped;
    }

    public String getTotal_wrong() {
        return total_wrong;
    }

    public void setTotal_wrong(String total_wrong) {
        this.total_wrong = total_wrong;
    }

    public String getTotal_speed() {
        return total_speed;
    }

    public void setTotal_speed(String total_speed) {
        this.total_speed = total_speed;
    }

    public String getTotal_timetaken() {
        return total_timetaken;
    }

    public void setTotal_timetaken(String total_timetaken) {
        this.total_timetaken = total_timetaken;
    }
}
