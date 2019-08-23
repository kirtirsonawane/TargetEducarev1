package com.targeteducare.Classes;

public class Course {
    int course_id=0;
    String coursename_inmarathi="";
    String course_name="";

    public Course(){

    }

    public Course(int id,String name ,String coursename_inmarathi){
        this.coursename_inmarathi=coursename_inmarathi;
        this.course_name=name;
        this.course_id=id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCoursename_inmarathi() {
        return coursename_inmarathi;
    }

    public void setCoursename_inmarathi(String coursename_inmarathi) {
        this.coursename_inmarathi = coursename_inmarathi;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
